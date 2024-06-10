package ru.practicum.main.service.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "friends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friend_id")
    private Set<Long> friends;
}
