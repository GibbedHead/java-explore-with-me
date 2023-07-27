package ru.practicum.explorewithme.ewmservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
