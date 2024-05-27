package ru.practicum.main.service.dto.compilation;

import lombok.*;
import ru.practicum.main.service.dto.event.EventShortResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CompilationResponse {
    private Long id;
    private List<EventShortResponse> events;
    private boolean pinned;
    private String title;
}
