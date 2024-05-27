package ru.practicum.main.service.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserShortResponse {
    private Long id;
    private String name;
}
