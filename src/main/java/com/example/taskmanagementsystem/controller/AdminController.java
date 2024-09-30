package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.CreateTaskDto;
import com.example.taskmanagementsystem.dto.Response;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.utils.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update")
    public ResponseEntity<Response> updateTask(@RequestBody CreateTaskDto request) {
        log.info("[#updateTask] is calling");
        try {
            CreateTaskDto createTaskDto = taskService.updateTaskAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Updated successfully", createTaskDto));
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("Task is not updated" + exception.getMessage(), null));
        }
    }
}
