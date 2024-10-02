package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.dto.TaskDto;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tasks")
@AllArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @PostMapping(value = "/create")
    public ResponseEntity<Response> createTask(@RequestBody CreateTaskDto request) {
        log.info("[#createTask] is calling");
        try {
            taskService.createTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Created successfully", request));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Task is not created" + exception.getMessage(), null));
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Response> updateTask(@RequestBody CreateTaskDto request) {
        log.info("[#updateTask] is calling");
        try {
            CreateTaskDto createTaskDto = taskService.updateTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Updated successfully", createTaskDto));
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(exception.getMessage(), null));
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        log.info("[deleteTask] is calling");
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @GetMapping("/get-task/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        log.info("[#getById] task with ID: {}", id);
        try {
            return ResponseEntity.ok(new Response("Successfully got task with id", taskService.getById(id)));
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Task with id is not found", null));
        }
    }

    @GetMapping("/get-all")
    public List<TaskDto> getAllTasks(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam int size,
                                     @RequestParam(required = false, defaultValue = "priority,ASC") String[] sort) {
        return taskService.getAll(page, size, sort);
    }


    @PutMapping("/assign/{taskId}/{userId}")
    public ResponseEntity<Response> assignTaskToUser(@PathVariable Long taskId,
                                                     @PathVariable Long userId) {
        try {
            TaskDto taskDto = taskService.assignTaskToUser(taskId, userId);
            return ResponseEntity.ok(new Response("Successfully assign task to user", taskDto));
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(exception.getMessage(), null));
        }
    }
}