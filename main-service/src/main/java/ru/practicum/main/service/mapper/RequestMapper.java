package ru.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.service.dto.request.ParticipationRequestResponse;
import ru.practicum.main.service.model.Request;

@Component
public class RequestMapper {

    public ParticipationRequestResponse toParticipationRequestResponse(Request request) {
        return new ParticipationRequestResponse(request.getId(),
                request.getEventId(),
                request.getRequesterId(),
                request.getStatus(),
                request.getCreated());
    }
}
