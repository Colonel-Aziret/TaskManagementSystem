package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.entity.User;

public interface UserService {
    User create (User user);
    User getById(Long id);
    User getCurrentUser();
}
