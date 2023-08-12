package ru.practicum.explorewithme.ewmservice.user.repository;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.ewmservice.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE COALESCE(:ids, NULL) IS NULL OR u.id IN (:ids)")
    List<User> findByIdIn(@Nullable @Param("ids") List<Long> ids, Pageable pageable);
}
