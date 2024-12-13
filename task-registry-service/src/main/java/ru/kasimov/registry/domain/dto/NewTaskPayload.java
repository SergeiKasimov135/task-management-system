package ru.kasimov.registry.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.kasimov.registry.domain.model.Priority;
import ru.kasimov.registry.domain.model.Status;
import ru.kasimov.registry.domain.model.User;

public record NewTaskPayload(
        @NotNull(message = "")
        @Size(min = 1, max = 70, message = "")
        String title,

        String description,

        Status status,

        Priority priority,

        String comments,

        User author,

        User assignee
) {
}
