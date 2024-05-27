package ru.practicum.main.service.model;

import lombok.*;
import ru.practicum.main.service.enums.StateRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "event_id")
    private long eventId;
    @Column(name = "requester_id")
    private long requesterId;
    private StateRequest status;
    private LocalDateTime created;
}
