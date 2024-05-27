package ru.practicum.main.service.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewCategoryRequest {
    @NotBlank(message = "Поле name пустое или null")
    @Size(min = 1, max = 50, message = "Поле name должно быть от 1 до 50 символов")
    private String name;
}
