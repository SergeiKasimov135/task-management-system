package ru.kasimov.registry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kasimov.registry.domain.dto.EditTaskPayload;
import ru.kasimov.registry.domain.model.Priority;
import ru.kasimov.registry.domain.model.Status;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.domain.model.User;
import ru.kasimov.registry.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultTaskServiceTest {

    @InjectMocks
    private DefaultTaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ShouldSaveTask() {
        String title = "Test Task";
        String description = "Task Description";
        Status status = Status.WAITING;
        Priority priority = Priority.MID;
        String comments = "Some comments";
        String authorUsername = "author";
        String assigneeUsername = "assignee";

        User author = new User();
        author.setUsername(authorUsername);

        User assignee = new User();
        assignee.setUsername(assigneeUsername);

        when(userService.getByUsername(authorUsername)).thenReturn(author);
        when(userService.getByUsername(assigneeUsername)).thenReturn(assignee);

        Task savedTask = new Task();
        savedTask.setTitle(title);
        savedTask.setDescription(description);
        savedTask.setStatus(status);
        savedTask.setPriority(priority);
        savedTask.setComments(comments);
        savedTask.setAuthor(author);
        savedTask.setAssignee(assignee);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task task = taskService.createTask(title, description, status, priority, comments, authorUsername, assigneeUsername);

        assertNotNull(task);
        assertEquals(title, task.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void editTask_ShouldUpdateTask() {
        Long taskId = 1L;
        EditTaskPayload payload = new EditTaskPayload("Updated Title", null, null, null, null, null, null);
        Task task = new Task();
        task.setTitle("Old Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.editTask(taskId, payload);

        assertEquals("Updated Title", task.getTitle());
        verify(taskRepository).findById(taskId);
    }

    @Test
    void deleteTask_ShouldCallDeleteById() {
        Long taskId = 1L;

        taskService.deleteTask(taskId);

        verify(taskRepository).deleteById(taskId);
    }
}
