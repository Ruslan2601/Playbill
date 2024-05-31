package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.compilation.CompilationResponse;
import ru.practicum.main.service.dto.compilation.NewCompilationRequest;
import ru.practicum.main.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.exception.model.ConflictException;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.mapper.CategoryMapper;
import ru.practicum.main.service.mapper.CompilationMapper;
import ru.practicum.main.service.mapper.EventMapper;
import ru.practicum.main.service.mapper.UserMapper;
import ru.practicum.main.service.model.Compilation;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.repository.CompilationRepository;
import ru.practicum.main.service.repository.EventRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {

    public final CompilationRepository repository;
    public final CompilationMapper mapper;
    public final EventRepository eventRepository;
    public final EventMapper eventMapper;
    public final CategoryMapper categoryMapper;
    public final UserMapper userMapper;

    public List<CompilationResponse> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        List<CompilationResponse> response = new ArrayList<>();

        if (pinned == null) {
            compilations = repository.findAll(pageable).toList();
        } else {
            compilations = repository.findAllByPinned(pinned, pageable);
        }

        Set<Long> eventsIds = new HashSet<>();

        // Собираем все id для событий чтобы сделать 1 запрос
        for (Compilation compilation : compilations) {
            eventsIds.addAll(compilation.getEvents());
        }

        Map<Long, EventShortResponse> events = convertEventToShort(eventRepository.findAllById(eventsIds))
                .stream()
                .collect(Collectors.toMap(EventShortResponse::getId, event -> event));


        // Для каждой подборки выставляем их события и преобразовываем
        for (Compilation compilation : compilations) {
            List<EventShortResponse> eventShortResponses = new ArrayList<>();
            for (Long eventId : compilation.getEvents()) {
                eventShortResponses.add(events.get(eventId));
            }
            response.add(mapper.toCompilationResponse(compilation, eventShortResponses));
        }

        return response;
    }

    public CompilationResponse getCompilation(long compId) {
        Compilation compilation = checkCompilation(compId);
        return mapper.toCompilationResponse(compilation,
                convertEventToShort(eventRepository.findAllById(compilation.getEvents())));
    }

    @Transactional
    public CompilationResponse addCompilation(NewCompilationRequest newCompilationRequest) {
        List<Event> events = new ArrayList<>();
        if (newCompilationRequest.getEvents() != null) {
            events = checkEvents(newCompilationRequest.getEvents());
        }
        return mapper.toCompilationResponse(repository.save(mapper.toCompilation(newCompilationRequest)),
                convertEventToShort(events));
    }

    @Transactional
    public CompilationResponse updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = checkCompilation(compId);

        List<Event> events = new ArrayList<>();

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            events = checkEvents(updateCompilationRequest.getEvents());
            compilation.setEvents(updateCompilationRequest.getEvents());
        }

        return mapper.toCompilationResponse(repository.save(compilation),
                convertEventToShort(events));
    }

    @Transactional
    public void deleteCompilation(long compId) {
        checkCompilation(compId);
        repository.deleteById(compId);
    }

    private Compilation checkCompilation(long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotExistException("Compilation с id = " + compId + " не существует"));
    }

    private List<Event> checkEvents(Set<Long> eventsIds) {
        List<Event> events = eventRepository.findAllById(eventsIds);

        if (events.size() != eventsIds.size()) {
            throw new ConflictException("В списке событий есть несущетвующие события");
        }

        return events;
    }

    private List<EventShortResponse> convertEventToShort(List<Event> events) {
        return events
                .stream()
                .map(event -> eventMapper.toEventShortResponse(event,
                        categoryMapper.toCategoryDto(event.getCategory()),
                        userMapper.toUserShortResponse(event.getInitiator())))
                .collect(toList());
    }
}
