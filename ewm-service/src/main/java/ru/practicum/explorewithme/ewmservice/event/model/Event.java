package ru.practicum.explorewithme.ewmservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.user.model.User;

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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    String annotation;
    @NotNull
    String description;
    @NotNull
    String title;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @Embedded
    Location location;
    Boolean paid;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    EventState state;
}
