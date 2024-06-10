package ru.practicum.main.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.main.service.enums.StateEvent;
import ru.practicum.main.service.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findByInitiatorId(long userId, Pageable pageable);

    List<Event> findByIdInAndEventDateAfterAndStateOrderByEventDateAsc(Set<Long> eventIds, LocalDateTime eventDate, StateEvent state);
}
