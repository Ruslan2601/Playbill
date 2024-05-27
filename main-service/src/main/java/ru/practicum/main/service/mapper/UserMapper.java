package ru.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.service.dto.user.NewUserRequest;
import ru.practicum.main.service.dto.user.UserResponse;
import ru.practicum.main.service.dto.user.UserShortResponse;
import ru.practicum.main.service.model.User;

@Component
public class UserMapper {

    public User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }

    public UserShortResponse toUserShortResponse(User user) {
        return new UserShortResponse(user.getId(), user.getName());
    }
}
