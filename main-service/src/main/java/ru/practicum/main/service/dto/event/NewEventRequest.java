package ru.practicum.main.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.main.service.annotation.HoursAfterOrEquals;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewEventRequest {
    @NotBlank(message = "Поле annotation пустое или null")
    @Size(min = 20, max = 2000, message = "Поле annotation должно быть от 20 до 2000 символов")
    private String annotation;
    @NotNull(message = "Поле category является null")
    private Long category;
    @NotBlank(message = "Поле description пустое или null")
    @Size(min = 20, max = 7000, message = "Поле description должно быть от 20 до 7000 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Поле eventDate является null")
    @HoursAfterOrEquals(hours = 2, message = "Дата начала не может быть раньше, чем за 2 часа от настоящего")
    private LocalDateTime eventDate;
    @NotNull(message = "Поле location является null")
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero(message = "Поле participantLimit отрицательное")
    private int participantLimit;
    @Value("true")
    private Boolean requestModeration;
    @NotBlank(message = "Поле title пустое или null")
    @Size(min = 3, max = 120, message = "Поле title должно быть от 3 до 120 символов")
    private String title;
}
