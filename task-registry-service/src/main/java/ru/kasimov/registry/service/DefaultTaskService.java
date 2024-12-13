package ru.kasimov.registry.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kasimov.registry.domain.dto.EditTaskPayload;
import ru.kasimov.registry.domain.model.Priority;
import ru.kasimov.registry.domain.model.Status;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.domain.model.User;
import ru.kasimov.registry.repository.TaskRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    @Override
    public Iterable<Task> findAllTasks(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.taskRepository.findAllByTitleLikeIgnoringCase("%" + filter + "%");
        } else {
            return this.taskRepository.findAll();
        }
    }

    @Override
    public Optional<Task> findTask(Long taskId) {
        return this.taskRepository.findById(taskId);
    }

    @Override
    @Transactional
    public Task createTask(String title, String description,
                           Status status, Priority priority,
                           String comments, String authorUsername, String assigneeUsername) {
        User author = this.userService.getByUsername(authorUsername);
        User assignee = this.userService.getByUsername(assigneeUsername);

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .comments(comments)
                .author(author)
                .assignee(assignee)
                .build();

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public void editTask(Long taskId, EditTaskPayload payload) {
        Optional<Task> optionalTask = this.taskRepository.findById(taskId);

        optionalTask.ifPresentOrElse(task -> {
            if (payload.title() != null) {
                task.setTitle(payload.title());
            }
            if (payload.description() != null) {
                task.setDescription(payload.description());
            }
            if (payload.status() != null) {
                task.setStatus(payload.status());
            }
            if (payload.priority() != null) {
                task.setPriority(payload.priority());
            }
            if (payload.comments() != null) {
                task.setComments(payload.comments());
            }
            if (payload.author() != null) {
                User author = this.userService.getByUsername(payload.author().getUsername());
                task.setAuthor(author);
            }
            if (payload.assignee() != null) {
                User assignee = this.userService.getByUsername(payload.assignee().getUsername());
                task.setAssignee(assignee);
            }
        }, () -> {
            throw new NoSuchElementException();
        });
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        this.taskRepository.deleteById(taskId);
    }

    @Override
    public Page<Task> findTasksByAuthor(String authorUsername, Pageable pageable) {
        User author = this.userService.getByUsername(authorUsername);

        return this.taskRepository.findByAuthor(author, pageable);
    }

    @Override
    public Page<Task> findTasksByAssignee(String assigneeUsername, Pageable pageable) {
        User assignee = this.userService.getByUsername(assigneeUsername);

        return this.taskRepository.findByAssignee(assignee, pageable);
    }
}
