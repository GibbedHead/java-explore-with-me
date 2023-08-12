package ru.practicum.explorewithme.ewmservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(Long userId);

    Long countByEventAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEvent(Long eventId);

    List<Request> findByIdIn(List<Long> ids);
}
