package ru.kasimov.registry.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_task", schema = "registry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries(
        @NamedQuery(
                name = "Task.findAllByTitleLikeIgnoringCase",
                query = "select task from Task task where task.title ilike :filter"
        )
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_title")
    @NotNull
    @Size(min = 1, max = 70)
    private String title;

    @Column(name = "c_description")
    private String description;

    @Column(name = "c_status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "c_priority")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "c_comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "с_author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "с_assignee_id")
    private User assignee;

}
