package ru.practicum.main.service.dto.event;

import lombok.*;
import ru.practicum.main.service.enums.StatusRequests;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusRequests status;
}
