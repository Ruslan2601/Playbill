package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.main.service.dto.event.EventRequestStatusUpdateResponse;
import ru.practicum.main.service.dto.request.ParticipationRequestResponse;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.enums.StateRequest;
import ru.practicum.main.service.enums.StatusRequests;
import ru.practicum.main.service.exception.model.ConflictException;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.mapper.RequestMapper;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.Request;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.repository.RequestRepository;
import ru.practicum.main.service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

    private final RequestRepository repository;
    private final RequestMapper mapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestResponse> getRequests(long userId) {
        checkUser(userId);

        return repository.findAllByRequesterId(userId)
                .stream()
                .map(mapper::toParticipationRequestResponse)
                .collect(toList());
    }

    public List<ParticipationRequestResponse> getRequestsOnEvent(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);

        return repository.findAllByEventId(eventId)
                .stream()
                .map(mapper::toParticipationRequestResponse)
                .collect(toList());
    }

    @Transactional
    public ParticipationRequestResponse addRequest(long userId, long eventId) {
        checkUser(userId);
        Event event = checkEvent(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("User с id = " + userId + " является инициатором");
        }
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Event с id = " + eventId + " не опубликовано");
        }
        if (repository.findAllByEventId(eventId).stream().map(Request::getRequesterId).collect(Collectors.toSet()).contains(userId)) {
            throw new ConflictException("User с id = " + userId + " уже в списке участников");
        }
        if (event.getParticipantLimit() != 0
                && event.getParticipantLimit() <= repository.findAllByStatusAndEventId(StateRequest.CONFIRMED, eventId).size()) {
            throw new ConflictException("Event с id = " + eventId + " имеет максимальное количество участников");
        }

        Request request = new Request();
        request.setEventId(eventId);
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        if (event.isRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(StateRequest.PENDING);
        } else {
            request.setStatus(StateRequest.CONFIRMED);
        }

        return mapper.toParticipationRequestResponse(repository.save(request));
    }

    @Transactional
    public ParticipationRequestResponse cancelRequest(long userId, long requestId) {
        checkUser(userId);
        Request request = repository.findById(requestId)
                .orElseThrow(() -> new NotExistException("Request с id = " + requestId + " не существует"));

        request.setStatus(StateRequest.CANCELED);

        return mapper.toParticipationRequestResponse(repository.save(request));
    }

    @Transactional
    public EventRequestStatusUpdateResponse updateRequestStatusOnEvent(long userId,
                                                                       long eventId,
                                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        List<Request> requests = repository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());

        // Если подтверждение не требуется или кочичесвто участников неограничено возвращаем ответ
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            return getAllEventRequests(eventId);
        }

        // Проверяем что все запросы состоянии ожидания
        for (Request request : requests) {
            if (!request.getStatus().equals(StateRequest.PENDING)) {
                throw new ConflictException("Request с id = " + request.getRequesterId() + " находиться в status = " + request.getStatus());
            }
        }

        // Отклоняем все запросы в случае статуса REJECTED
        if (eventRequestStatusUpdateRequest.getStatus().equals(StatusRequests.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(StateRequest.REJECTED);
            }
            repository.saveAll(requests);
            return getAllEventRequests(eventId);
        }

        Set<Long> confirmedRequests = repository.findAllByStatusAndEventId(StateRequest.CONFIRMED, eventId)
                .stream().map(Request::getRequesterId).collect(Collectors.toSet());

        // Проверяем что есть места для подтверждения
        if (confirmedRequests.size() >= event.getParticipantLimit()
                && eventRequestStatusUpdateRequest.getStatus().equals(StatusRequests.CONFIRMED)) {
            throw new ConflictException("Event с id = " + eventId + " имеет максимальное количество участников");
        }

        for (Request request : requests) {
            if (confirmedRequests.size() <= event.getParticipantLimit()) {
                request.setStatus(StateRequest.CONFIRMED);
                confirmedRequests.add(request.getId());
            } else {
                request.setStatus(StateRequest.REJECTED);
            }
        }

        return getAllEventRequests(eventId);
    }

    private void checkUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException("User с id = " + userId + " не существует"));
    }

    private Event checkEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotExistException("Event с id = " + eventId + " не существует"));
    }

    private EventRequestStatusUpdateResponse getAllEventRequests(long eventId) {
        List<Request> requests = repository.findAllByEventId(eventId);
        EventRequestStatusUpdateResponse response = new EventRequestStatusUpdateResponse(new ArrayList<>(), new ArrayList<>());

        for (Request request : requests) {
            if (request.getStatus().equals(StateRequest.CONFIRMED)) {
                response.getConfirmedRequests().add(mapper.toParticipationRequestResponse(request));
            }
            if (request.getStatus().equals(StateRequest.REJECTED)) {
                response.getRejectedRequests().add(mapper.toParticipationRequestResponse(request));
            }
        }

        return response;
    }
}
