package ru.kasimov.registry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kasimov.registry.domain.dto.NewTaskPayload;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.service.TaskService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/registry-api/tasks")
@Tag(name = "Манипуляции над несколькими задачами")
public class TasksRestController {

    private final TaskService taskService;

    @GetMapping("list")
    @Operation(summary = "Получение списка задач")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Iterable<Task> findTasks(@RequestParam(name = "filter", required = false) String filter) {
        return this.taskService.findAllTasks(filter);
    }

    @PostMapping
    @Operation(summary = "Создание задачи")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@Validated @RequestBody NewTaskPayload payload,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException bindException) {
                throw bindException;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Task task = this.taskService.createTask(
                    payload.title(), payload.description(),
                    payload.status(), payload.priority(),
                    payload.comments(), payload.author().getUsername(),
                    payload.assignee().getUsername()
            );

            return ResponseEntity
                    .created(uriComponentsBuilder
                        .replacePath("v1/registry-api/tasks/{taskId}")
                        .build(Map.of("taskId", task.getId())))
                    .body(task);
        }
    }

    @GetMapping("by-author")
    @Operation(summary = "Поиск задач по автору")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<Task> findTasksByAuthor(@RequestParam String authorUsername,
                                        @RequestParam Integer page,
                                        @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.taskService.findTasksByAuthor(authorUsername, pageable);
    }

    @GetMapping("by-assignee")
    @Operation(summary = "Поиск задач по исполнителю")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<Task> findTasksByAssignee(@RequestParam String assigneeUsername,
                                          @RequestParam Integer page,
                                          @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.taskService.findTasksByAssignee(assigneeUsername, pageable);
    }

}
