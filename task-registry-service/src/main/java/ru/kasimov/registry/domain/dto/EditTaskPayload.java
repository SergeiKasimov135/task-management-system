package ru.kasimov.registry.domain.dto;

import ru.kasimov.registry.domain.model.Priority;
import ru.kasimov.registry.domain.model.Status;
import ru.kasimov.registry.domain.model.User;

public record EditTaskPayload(

        String title,

        String description,

        Status status,

        Priority priority,

        String comments,

        User author,

        User assignee

) {
}
