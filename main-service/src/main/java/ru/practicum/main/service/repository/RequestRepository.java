package ru.practicum.main.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.enums.StateRequest;
import ru.practicum.main.service.model.Request;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long userId);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByStatusAndEventId(StateRequest stateRequest, long eventId);

    List<Request> findByRequesterIdAndStatus(long requesterId, StateRequest status);

    List<Request> findByRequesterIdInAndStatus(Set<Long> requesterIds, StateRequest status);
}
