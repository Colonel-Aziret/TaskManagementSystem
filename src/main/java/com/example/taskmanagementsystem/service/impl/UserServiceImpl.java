package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.enums.Role;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    // Сохранение пользователя
    public User save(User user) {
        return userRepository.save(user);
    }


    // Создание пользователя
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        log.info("Create user with username: {}", user.getUsername());
        return save(user);
    }

    @Override
    public User getById(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Получение пользователя по имени пользователя
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    // Получение пользователя по имени пользователя

    // Получение текущего пользователя
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    // Выдача прав администратора текущему пользователю
    @Deprecated
    public User getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ADMIN);
        save(user);
        return user;
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
