package ru.practicum.main.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.service.annotation.HoursAfterOrEquals;
import ru.practicum.main.service.enums.StateUserAction;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Поле annotation должно быть от 20 до 2000 символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Поле description должно быть от 20 до 7000 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @HoursAfterOrEquals(hours = 2, message = "Дата начала не может быть раньше, чем за 2 часа от настоящего")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero(message = "Поле participantLimit отрицательное")
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateUserAction stateAction;
    @Size(min = 3, max = 120, message = "Поле title должно быть от 3 до 120 символов")
    private String title;
}
