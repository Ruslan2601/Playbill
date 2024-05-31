package ru.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.service.dto.category.CategoryResponse;
import ru.practicum.main.service.dto.event.EventFullResponse;
import ru.practicum.main.service.dto.event.EventShortResponse;
import ru.practicum.main.service.dto.event.LocationDto;
import ru.practicum.main.service.dto.event.NewEventRequest;
import ru.practicum.main.service.dto.user.UserShortResponse;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.model.Event;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public Event toEvent(NewEventRequest newEventRequest) {
        Event event = new Event();
        event.setAnnotation(newEventRequest.getAnnotation());
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventRequest.getDescription());
        event.setEventDate(newEventRequest.getEventDate());
        event.setLat(newEventRequest.getLocation().getLat());
        event.setLon(newEventRequest.getLocation().getLon());
        event.setPaid(newEventRequest.isPaid());
        event.setParticipantLimit(newEventRequest.getParticipantLimit());
        if (newEventRequest.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(newEventRequest.getRequestModeration());
        }
        event.setState(StateEvent.PENDING);
        event.setTitle(newEventRequest.getTitle());
        return event;
    }

    public EventShortResponse toEventShortResponse(Event event,
                                                   CategoryResponse categoryResponse,
                                                   UserShortResponse userShortDto) {
        EventShortResponse eventShortResponse = new EventShortResponse();
        eventShortResponse.setAnnotation(event.getAnnotation());
        eventShortResponse.setCategory(categoryResponse);
        if (event.getConfirmedRequests() != null) {
            eventShortResponse.setConfirmedRequests((long) event.getConfirmedRequests().size());
        }
        eventShortResponse.setEventDate(event.getEventDate());
        eventShortResponse.setId(event.getId());
        eventShortResponse.setInitiator(userShortDto);
        eventShortResponse.setPaid(event.isPaid());
        eventShortResponse.setTitle(event.getTitle());
        eventShortResponse.setViews(event.getViews());
        return eventShortResponse;
    }

    public EventFullResponse toEventFullResponse(Event event,
                                                 CategoryResponse categoryResponse,
                                                 UserShortResponse userShortDto) {
        EventFullResponse eventFullResponse = new EventFullResponse();
        eventFullResponse.setId(eventFullResponse.getId());
        eventFullResponse.setAnnotation(event.getAnnotation());
        eventFullResponse.setCategory(categoryResponse);
        if (event.getConfirmedRequests() != null) {
            eventFullResponse.setConfirmedRequests((long) event.getConfirmedRequests().size());
        }
        eventFullResponse.setId(event.getId());
        eventFullResponse.setCreatedOn(event.getCreatedOn());
        eventFullResponse.setDescription(event.getDescription());
        eventFullResponse.setEventDate(event.getEventDate());
        eventFullResponse.setInitiator(userShortDto);
        eventFullResponse.setLocation(new LocationDto(event.getLat(), event.getLon()));
        eventFullResponse.setPaid(event.isPaid());
        eventFullResponse.setParticipantLimit(event.getParticipantLimit());
        eventFullResponse.setPublishedOn(event.getPublishedOn());
        eventFullResponse.setRequestModeration(event.isRequestModeration());
        eventFullResponse.setState(event.getState());
        eventFullResponse.setTitle(event.getTitle());
        eventFullResponse.setViews(event.getViews());
        return eventFullResponse;
    }
}
