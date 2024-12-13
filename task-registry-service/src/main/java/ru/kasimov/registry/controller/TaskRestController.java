package ru.kasimov.registry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kasimov.registry.domain.dto.EditTaskPayload;
import ru.kasimov.registry.domain.model.Task;
import ru.kasimov.registry.service.TaskService;

import java.security.Principal;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/registry-api/tasks/{taskId:\\d+}")
@Tag(name = "Манипуляция над одной задачей")
public class TaskRestController {

    private final TaskService taskService;

    private final MessageSource messageSource;

    @ModelAttribute("task")
    public Task task(@PathVariable("taskId") Long taskId) {
        return this.taskService.findTask(taskId).orElseThrow(
                () -> new NoSuchElementException("{task.errors.not_found}")
        );
    }

    @GetMapping
    @Operation(summary = "поиск задачи по id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Task findTask(@ModelAttribute("task") Task task) {
        return task;
    }

    @PatchMapping
    @Operation(summary = "редактирование задачи")
    @PreAuthorize("""
        hasRole('ADMIN') or
        @defaultSecurityService.isTaskAssignedToUser(#taskId, principal.username)
    """)
    public ResponseEntity<?> editTask(@PathVariable("taskId") Long taskId,
                                      @Validated @RequestBody EditTaskPayload payload,
                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException bindException) {
                throw bindException;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.taskService.editTask(taskId, payload);

            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    @Operation(summary = "удаление задачи")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
        this.taskService.deleteTask(taskId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(
                                exception.getMessage(),
                                new Object[0],
                                exception.getMessage(),
                                locale)));
    }

}
