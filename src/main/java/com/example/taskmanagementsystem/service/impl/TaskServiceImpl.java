package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.enums.Role;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.service.UserService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import com.example.taskmanagementsystem.utils.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
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
        User user = userService.getCurrentUser();
        if (task.getUser() != null && user.getId().equals(task.getUser().getId()) || user.getRole().equals(Role.ADMIN)) {
            log.info("userId, username, role: {}, {}, {}", user.getId(), user.getUsername(), user.getRole());
            task = buildTask(request, task);
            CreateTaskDto createTaskDto = new CreateTaskDto(task);
            log.info("updated task with ID: {}", task.getId());
            taskRepository.save(task);
            return createTaskDto;
        }
        throw new ObjectNotFoundException("User cannot update this task");
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
    public TaskDto getById(Long id) throws ObjectNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Task with id is not found"));
        return taskMapper.entityToDto(task);
    }

    @Override
    public List<TaskDto> getAll(int page, int size, String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        // Проверяем, если сортировка по приоритету
        if (sort[0].equalsIgnoreCase("priority")) {
            // Сортируем по полю priority
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "priority"));
            Page<Task> taskDtoPage = taskRepository.findAll(pageable);
            List<Task> tasks = taskDtoPage.getContent();
            return taskMapper.entityToDtoList(tasks);
        }
        // Стандартная сортировка
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Task> taskDtoPage = taskRepository.findAll(pageable);
        List<Task> tasks = taskDtoPage.getContent();
        return taskMapper.entityToDtoList(tasks);
    }

    @Override
    public TaskDto assignTaskToUser(Long taskId, Long userId) throws ObjectNotFoundException {
        log.info("assign task with id: {} to user with id: {}", taskId, userId);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ObjectNotFoundException("Task with id is not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User with id is not found"));
        task.setUser(user);
        taskRepository.save(task);
        return taskMapper.entityToDto(task);
    }
}
