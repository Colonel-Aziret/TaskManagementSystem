package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            taskService.updateTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Updated successfully", request));
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Task is not updated" + exception.getMessage(), null));
        }
    }
}