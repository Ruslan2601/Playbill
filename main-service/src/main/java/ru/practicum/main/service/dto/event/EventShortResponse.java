package ru.practicum.main.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.dto.user.UserShortResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EventShortResponse {
    private String annotation;
    private CategoryResponse category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortResponse initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
