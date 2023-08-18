package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.event.model.ModerationComment;

public interface ModerationCommentRepository extends JpaRepository<ModerationComment, Long> {

}
