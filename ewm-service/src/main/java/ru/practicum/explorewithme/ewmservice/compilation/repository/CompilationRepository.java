package ru.practicum.explorewithme.ewmservice.compilation.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation_;

public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        JpaSpecificationExecutor<Compilation> {

    interface Specifications {
        static Specification<Compilation> byPinned(Boolean pinned) {
            return (root, query, builder) -> {
                if (pinned != null) {
                    return builder.equal(root.get(Compilation_.pinned), pinned);
                } else {
                    return builder.and();
                }
            };
        }
    }
}
