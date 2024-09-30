package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void createTask(CreateTaskDto request);

    CreateTaskDto updateTaskAdmin(CreateTaskDto request) throws ObjectNotFoundException;
    CreateTaskDto updateTaskUser(CreateTaskDto request, Long id) throws ObjectNotFoundException;

    void deleteTask(Long id) throws ObjectNotFoundException;

    TaskDto getById(Long id) throws ObjectNotFoundException;

    List<TaskDto> getAll(int page, int size, String[] sort);

    TaskDto assignTaskToUser(Long taskId, Long userId) throws ObjectNotFoundException;
}
