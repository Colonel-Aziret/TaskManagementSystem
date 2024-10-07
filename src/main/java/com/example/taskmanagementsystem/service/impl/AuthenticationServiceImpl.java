package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.JwtAuthenticationResponse;
import com.example.taskmanagementsystem.dto.SignInRequest;
import com.example.taskmanagementsystem.dto.SignUpRequest;
import com.example.taskmanagementsystem.enums.Role;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl {
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    // Регистрация пользователя
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    // Аутентификация пользователя
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public String addUserWithOAuth2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            log.info("OAuth2 Token received");  // Добавляем логирование
            OAuth2User oAuth2User = oauthToken.getPrincipal();
            if (oAuth2User != null) {
                Map<String, Object> attributes = oAuth2User.getAttributes();
                log.info("OAuth2 User Attributes: {}", attributes);  // Логируем атрибуты
                Optional<User> optionalUser = userRepository.findByIdFromOAuth2((String) attributes.get("sub"));
                if (optionalUser.isEmpty()) {
                    User user = new User();
                    user.setUsername((String) attributes.get("given_name"));
                    String password = passwordEncoder.encode(UUID.randomUUID().toString());
                    user.setPassword(password);
                    user.setEmail((String) attributes.get("email"));
                    user.setRole(Role.USER);
                    userRepository.save(user);
                }
                return "Hello, " + attributes.get("given_name");
            }
        }
        return "No OAuth2AuthenticationToken found";
    }
}

