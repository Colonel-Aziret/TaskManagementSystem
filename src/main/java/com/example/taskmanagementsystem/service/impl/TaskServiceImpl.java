package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.service.UserService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public void createTask(CreateTaskDto request) {
        log.info("[#createTask] is calling");
        Task task = buildTask(request, new Task());
        taskRepository.save(task);
        log.info("saved task with ID: {}", task.getId());
    }

    @Override
    public CreateTaskDto updateTask(CreateTaskDto request) throws ObjectNotFoundException {
        log.info("[#updateTask] is calling");
        Task task = taskRepository.findById(request.getId()).orElseThrow(() -> new ObjectNotFoundException("Task is not found"));
        task = buildTask(request, task);
        CreateTaskDto createTaskDto = new CreateTaskDto(task);
        log.info("updated task with ID: {}", task.getId());
        taskRepository.save(task);
        return createTaskDto;
    }

    private Task buildTask(CreateTaskDto request, Task task) {
        User user = userService.getById(request.getUserId());
        return task.toBuilder().
                title(request.getTitle())
                .description(request.getDescription())
                .dueTime(request.getDueTime())
                .user(user)
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();
    }

    @Override
    public Response delete(Long id) {
        return null;
    }

    @Override
    public Response getById(Long id) {
        return null;
    }

    @Override
    public Response getAll() {
        return null;
    }
}
