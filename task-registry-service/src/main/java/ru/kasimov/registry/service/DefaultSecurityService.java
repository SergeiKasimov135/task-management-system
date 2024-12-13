package ru.kasimov.registry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kasimov.registry.domain.model.Task;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DefaultSecurityService {

    private final TaskService taskService;

    public boolean isTaskAssignedToUser(Long taskId, String username) {
        Task task = this.taskService.findTask(taskId)
                .orElseThrow(() -> new NoSuchElementException("Задача не найдена"));

        return task.getAssignee().getUsername().equals(username);
    }
}
