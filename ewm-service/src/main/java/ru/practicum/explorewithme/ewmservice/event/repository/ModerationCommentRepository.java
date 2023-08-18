package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.ModerationComment;

import java.util.List;

public interface ModerationCommentRepository extends JpaRepository<ModerationComment, Long> {

    List<ModerationComment> findByEvent(Event event);

}
