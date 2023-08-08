package ru.practicum.explorewithme.ewmservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.Event_;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.model.Request_;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {
    List<Event> findByInitiatorId(Long userId, Pageable pageable);


    interface Specifications {
        static Specification<Event> byUserIn(List<Long> users) {
            return (root, query, builder) -> {
                if (users != null && !users.isEmpty()) {
                    return root.get(Event_.id).in(users);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byStateIn(List<EventState> states) {
            return (root, query, builder) -> {
                if (states != null && !states.isEmpty()) {
                    return root.get(Event_.state).in(states);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byCategoryIn(List<Long> categories) {
            return (root, query, builder) -> {
                if (categories != null && !categories.isEmpty()) {
                    return root.get(Event_.category).in(categories);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byRangeStart(LocalDateTime rangeStart) {
            return (root, query, builder) -> {
                if (rangeStart != null) {
                    return builder.greaterThanOrEqualTo(root.get(Event_.eventDate), rangeStart);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byRangeEnd(LocalDateTime rangeEnd) {
            return (root, query, builder) -> {
                if (rangeEnd != null) {
                    return builder.lessThanOrEqualTo(root.get(Event_.eventDate), rangeEnd);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byAnnotationAndDescriptionIgnoreCases(String text) {
            return (root, query, builder) -> {
                if (text != null) {
                    Predicate byAnnotation = builder.like(
                            builder.lower(root.get(Event_.annotation)),
                            "%" + text.toLowerCase() + "%"
                    );
                    Predicate byDescription = builder.like(
                            builder.lower(root.get(Event_.description)),
                            "%" + text.toLowerCase() + "%"
                    );
                    return builder.or(byAnnotation, byDescription);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byPaid(Boolean paid) {
            return (root, query, builder) -> {
                if (paid != null) {
                    return builder.equal(root.get(Event_.paid), paid);
                } else {
                    return builder.and();
                }
            };
        }

        static Specification<Event> byAvailability(Boolean onlyAvailable) {
            return (root, query, builder) -> {
                if (onlyAvailable != null && onlyAvailable) {
                    Subquery<Long> confirmedRequestCountSubquery = query.subquery(Long.class);
                    Root<Request> requestRoot = confirmedRequestCountSubquery.from(Request.class);
                    confirmedRequestCountSubquery.select(builder.count(requestRoot));
                    confirmedRequestCountSubquery.where(
                            builder.and(
                                    builder.equal(requestRoot.get(Request_.event), root.get(Event_.id)),
                                    builder.equal(requestRoot.get(Request_.status), RequestStatus.CONFIRMED)
                            )
                    );
                    return builder.lt(
                            root.get(Event_.participantLimit),
                            confirmedRequestCountSubquery.getSelection()
                    );
                } else {
                    return builder.and();
                }
            };
        }
    }
}
