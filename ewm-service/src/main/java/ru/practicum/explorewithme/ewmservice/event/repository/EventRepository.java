package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
