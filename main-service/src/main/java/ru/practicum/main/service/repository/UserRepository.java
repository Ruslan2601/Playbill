package ru.practicum.main.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}
