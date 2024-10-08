package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public String getMessage(@PathVariable Long userId) {
        return userService.getMessage(userId);
    }
}
