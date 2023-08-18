package ru.practicum.explorewithme.ewmservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "moderation_comments")
public class ModerationComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @NotNull
    String comment;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
}
