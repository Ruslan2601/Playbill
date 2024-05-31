package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.user.NewUserRequest;
import ru.practicum.main.service.dto.user.UserResponse;
import ru.practicum.main.service.exception.model.NotExistException;
import ru.practicum.main.service.mapper.UserMapper;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

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

    @Transactional
    public UserResponse addUser(NewUserRequest newUserRequest) {
        return mapper.toUserResponse(repository.save(mapper.toUser(newUserRequest)));
    }

    @Transactional
    public void deleteUser(long userId) {
        repository.findById(userId).orElseThrow(() -> new NotExistException("User с id = " + userId + " не существует"));
        repository.deleteById(userId);
    }
}
