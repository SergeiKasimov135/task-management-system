package ru.kasimov.registry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.domain.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(name = "Task.findAllByTitleLikeIgnoringCase")
    Iterable<Task> findAllByTitleLikeIgnoringCase(@Param("filter") String filter);

    Page<Task> findByAuthor(User user, Pageable pageable);

    Page<Task> findByAssignee(User user, Pageable pageable);

}
