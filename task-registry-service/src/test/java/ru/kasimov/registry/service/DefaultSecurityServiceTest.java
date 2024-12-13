package ru.kasimov.registry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.domain.model.User;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultSecurityServiceTest {

    @InjectMocks
    private DefaultSecurityService securityService;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isTaskAssignedToUser_ShouldReturnTrue() {
        Long taskId = 1L;
        String username = "testuser";
        User assignee = new User();
        assignee.setUsername(username);
        Task task = new Task();
        task.setAssignee(assignee);

        when(taskService.findTask(taskId)).thenReturn(Optional.of(task));

        boolean result = securityService.isTaskAssignedToUser(taskId, username);

        assertTrue(result);
    }

    @Test
    void isTaskAssignedToUser_ShouldThrowException_WhenTaskNotFound() {
        Long taskId = 1L;
        String username = "testuser";

        when(taskService.findTask(taskId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            securityService.isTaskAssignedToUser(taskId, username);
        });
    }
}
