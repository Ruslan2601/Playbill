package ru.practicum.main.service.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.enums.StateSortEvent;
import ru.practicum.main.service.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventSpecification {

    public static Specification<Event> hasUsers(List<Long> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> root.get("initiator").in(users));
    }

    public static Specification<Event> hasStates(List<StateEvent> states) {
        if (states == null || states.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> root.get("state").in(states));
    }

    public static Specification<Event> hasCategories(List<Long> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> root.get("category").in(categories));
    }

    public static Specification<Event> hasRangeStart(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart));
    }

    public static Specification<Event> hasRangeEnd(LocalDateTime rangeEnd) {
        if (rangeEnd == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd));
    }

    public static Specification<Event> hasRangeDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null || rangeEnd == null) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("eventDate"), rangeStart, rangeEnd));
    }

    public static Specification<Event> hasTextAnnotation(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"));
    }

    public static Specification<Event> hasTextDescription(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%"));
    }

    public static Specification<Event> hasPaid(Boolean paid) {
        if (paid == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid));
    }

    public static Specification<Event> hasLimit(boolean onlyAvailable) {
        if (!onlyAvailable) {
            return null;
        }
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lt(criteriaBuilder.size(root.get("confirmedRequests")), root.get("participantLimit")));
    }

    public static Specification<Event> hasPublished() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), StateEvent.PUBLISHED));
    }

    public static Specification<Event> hasSort(StateSortEvent sort) {
        if (sort == null) {
            return null;
        }

        String field;

        if (sort.equals(StateSortEvent.EVENT_DATE)) {
            field = "eventDate";
        } else {
            field = "views";
        }

        return ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get(field)));
            return criteriaBuilder.conjunction();
        });
    }
}
