package ru.kasimov.registry.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.kasimov.registry.domain.dto.EditTaskPayload;
import ru.kasimov.registry.domain.model.Priority;
import ru.kasimov.registry.domain.model.Status;
import ru.kasimov.registry.domain.model.Task;

import java.util.Optional;

public interface TaskService {

    Iterable<Task> findAllTasks(String filter);

    Optional<Task> findTask(Long taskId);

    Task createTask(String title, String description,
                    Status status, Priority priority, String comments,
                    String authorUsername, String assigneeUsername);

    void editTask(Long taskId, EditTaskPayload payload);

    void deleteTask(Long taskId);

    Page<Task> findTasksByAuthor(String authorUsername, Pageable pageable);

    Page<Task> findTasksByAssignee(String assigneeUsername, Pageable pageable);
}
