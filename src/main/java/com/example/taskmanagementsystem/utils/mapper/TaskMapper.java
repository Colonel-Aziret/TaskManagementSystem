package com.example.taskmanagementsystem.utils.mapper;

import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    public TaskDto entityToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        if (task.getUser() != null) {
            taskDto.setUserId(task.getUser().getId());
        }
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueTime(task.getDueTime());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        taskDto.setPriority(task.getPriority());
        taskDto.setStatus(task.getStatus());
        return taskDto;
    }

    public List<TaskDto> entityToDtoList(List<Task> tasks) {
        return tasks.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
