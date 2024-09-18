package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.JwtAuthenticationResponse;
import com.example.taskmanagementsystem.dto.SignInRequest;
import com.example.taskmanagementsystem.dto.SignUpRequest;
import com.example.taskmanagementsystem.enums.Role;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.security.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserServiceImpl userService;
    private final JwtHandler handler;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        // Создаем пользователя
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        // Сохраняем пользователя в базе данных
        userService.create(user);

        // Генерируем JWT токен
        var jwt = handler.jwtGenerationToken(user);

        // Возвращаем JWT токен в ответе
        return new JwtAuthenticationResponse(jwt);
    }


    // Аутентификация пользователя
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = handler.jwtGenerationToken((User) user);
        return new JwtAuthenticationResponse(jwt);
    }
}
