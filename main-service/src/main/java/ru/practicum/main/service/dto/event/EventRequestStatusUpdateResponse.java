package ru.practicum.main.service.dto.event;

import lombok.*;
import ru.practicum.main.service.dto.request.ParticipationRequestResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EventRequestStatusUpdateResponse {
    private List<ParticipationRequestResponse> confirmedRequests;
    private List<ParticipationRequestResponse> rejectedRequests;
}
