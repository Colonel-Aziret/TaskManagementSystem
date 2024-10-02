package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final TaskService taskService;
}
