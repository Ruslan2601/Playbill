package ru.practicum.main.service.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewUserRequest {
    @NotBlank(message = "Поле email пустое или null")
    @Email(message = "Поле email не соответстует емеилу")
    @Size(min = 6, max = 254, message = "Поле email должно быть от 6 до 254 символов")
    private String email;
    @NotBlank(message = "Поле name пустое или null")
    @Size(min = 2, max = 250, message = "Поле name должно быть от 2 до 250 символов")
    private String name;
}
