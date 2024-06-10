package ru.practicum.main.service.dto.user;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private Set<Long> friends;
}
