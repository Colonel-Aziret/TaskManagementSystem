package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;

import java.util.List;

public interface TaskService {
    void createTask(CreateTaskDto request);

    CreateTaskDto updateTask(CreateTaskDto request) throws ObjectNotFoundException;

    void deleteTask(Long id) throws ObjectNotFoundException;

    Response getById(Long id);

    List<TaskDto> getAll();
}
