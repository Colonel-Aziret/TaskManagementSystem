package com.example.taskmanagementsystem.dto;

import com.example.taskmanagementsystem.enums.Priority;
import com.example.taskmanagementsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private Priority priority;
    private Status status;
}
