package com.example.taskmanagementsystem.dto;

import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.enums.Priority;
import com.example.taskmanagementsystem.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private Priority priority;
    private Status status;

    public CreateTaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueTime = task.getDueTime();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.userId = task.getUser().getId();
        this.priority = task.getPriority();
        this.status = task.getStatus();
    }

    public CreateTaskDto() {
    }
}
