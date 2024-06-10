package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.dto.user.NewUserRequest;
import ru.practicum.main.service.dto.user.UserResponse;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.enums.StateRequest;
import ru.practicum.main.service.exception.model.ConflictException;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.mapper.CategoryMapper;
import ru.practicum.main.service.mapper.EventMapper;
import ru.practicum.main.service.mapper.UserMapper;
import ru.practicum.main.service.model.Request;
import ru.practicum.main.service.model.User;
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
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final CategoryMapper categoryMapper;

    public List<UserResponse> getUsers(List<Long> ids, int from, int size) {

        Pageable pageable = PageRequest.of(from / size, size);
        Page<User> users;

        if (ids == null || ids.isEmpty()) {
            users = repository.findAll(pageable);
        } else {
            users = repository.findAllByIdIn(ids, pageable);
        }

        return users
                .stream()
                .map(mapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public List<EventShortResponse> getFriendRequests(long userId, long friendId) {
        checkUniqueIds(userId, friendId);
        User user = checkUser(userId);

        if (!user.getFriends().contains(friendId)) {
            throw new NotExistException("User с id = " + userId + " не имеет в друзьях friendId = " + friendId);
        }

        Set<Long> eventIds = requestRepository.findByRequesterIdAndStatus(friendId, StateRequest.CONFIRMED)
                .stream()
                .map(Request::getEventId)
                .collect(Collectors.toSet());

        return eventRepository
                .findByIdInAndEventDateAfterAndStateOrderByEventDateAsc(
                        eventIds,
                        LocalDateTime.now(),
                        StateEvent.PUBLISHED)
                .stream()
                .map(event -> eventMapper.toEventShortResponse(event,
                        categoryMapper.toCategoryDto(event.getCategory()),
                        mapper.toUserShortResponse(event.getInitiator())))
                .collect(toList());
    }

    public List<EventShortResponse> getFriendsRequests(long userId) {
        User user = checkUser(userId);

        if (user.getFriends().isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> eventIds = requestRepository.findByRequesterIdInAndStatus(user.getFriends(), StateRequest.CONFIRMED)
                .stream()
                .map(Request::getEventId)
                .collect(Collectors.toSet());

        return eventRepository
                .findByIdInAndEventDateAfterAndStateOrderByEventDateAsc(
                        eventIds,
                        LocalDateTime.now(),
                        StateEvent.PUBLISHED)
                .stream()
                .map(event -> eventMapper.toEventShortResponse(event,
                        categoryMapper.toCategoryDto(event.getCategory()),
                        mapper.toUserShortResponse(event.getInitiator())))
                .collect(toList());
    }

    @Transactional
    public UserResponse addUser(NewUserRequest newUserRequest) {
        return mapper.toUserResponse(repository.save(mapper.toUser(newUserRequest)));
    }

    @Transactional
    public UserResponse addFriend(long userId, long friendId) {
        checkUniqueIds(userId, friendId);
        User user = checkUser(userId);

        if (user.getFriends().contains(friendId)) {
            throw new ConflictException("User с id = " + userId + " уже является другом с friendId = " + friendId);
        }

        user.getFriends().add(checkUser(friendId).getId());

        return mapper.toUserResponse(repository.save(user));
    }

    @Transactional
    public void deleteUser(long userId) {
        repository.findById(userId).orElseThrow(() -> new NotExistException("User с id = " + userId + " не существует"));
        repository.deleteById(userId);
    }

    @Transactional
    public void deleteFriend(long userId, long friendId) {
        checkUniqueIds(userId, friendId);
        User user = checkUser(userId);

        if (!user.getFriends().remove(friendId)) {
            throw new NotExistException("User с id = " + userId + " не является другом с friendId = " + friendId);
        }

        repository.save(user);
    }

    private User checkUser(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotExistException("User с id = " + userId + " не существует"));
    }

    private void checkUniqueIds(long userId, long friendId) {
        if (userId == friendId) {
            throw new ConflictException("Указаны одинаковые id, userId = " + userId + ", friendId = " + friendId);
        }
    }
}
