package ru.practicum.explorewithme.ewmservice.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

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
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Column(name = "event_id")
    Long event;
    @NotNull
    @Column(name = "requester_id")
    Long requester;
    @NotNull
    LocalDateTime created;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
