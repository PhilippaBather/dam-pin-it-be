package com.batherphilippa.pin_it_app_be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.batherphilippa.pin_it_app_be.constants.ValidationMessages.VALIDATION_TASK_TITLE_NOT_BLANK;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class TaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private long id;

    @NotBlank(message = VALIDATION_TASK_TITLE_NOT_BLANK)
    @Size(min = 1, max = 250, message = "Comment required")
    @Column(name = "task_comment")
    private String comment;

    private LocalDate creationDate;

    @ManyToOne
    @JsonBackReference(value = "tasks_comments")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JsonBackReference(value = "tasks_users")
    @JoinColumn(name = "user_id")
    private User user;
}
