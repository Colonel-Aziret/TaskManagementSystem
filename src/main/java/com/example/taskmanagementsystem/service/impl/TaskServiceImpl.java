package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.service.UserService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import com.example.taskmanagementsystem.utils.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

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
    public void deleteTask(Long id) throws ObjectNotFoundException {
        log.info("Delete task with id: {}", id);
        if (taskRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException("Task with id is not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public Response getById(Long id) {
        return null;
    }

    @Override
    public List<TaskDto> getAll() {
        return taskMapper.entityToDtoList(taskRepository.findAll());
    }
}
