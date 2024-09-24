package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;

public interface TaskService {
    void createTask(CreateTaskDto request);

    CreateTaskDto updateTask(CreateTaskDto request) throws ObjectNotFoundException;

    Response delete(Long id);

    Response getById(Long id);

    Response getAll();
}
