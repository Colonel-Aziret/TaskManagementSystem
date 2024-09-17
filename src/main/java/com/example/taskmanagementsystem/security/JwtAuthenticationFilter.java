package com.example.taskmanagementsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Objects;
/*

1. Извлекает JWT-токен из заголовка Authorization HTTP-запроса.
2. Валидирует JWT-токен с помощью JwtTokenHandler.
3. Если токен валиден, извлекает имя пользователя из токена.
4. Загружает информацию о пользователе (UserDetails) с помощью UserDetailsService.
5. Создает объект аутентификации Spring Security (UsernamePasswordAuthenticationToken) на основе информации о пользователе.
6. Устанавливает аутентификацию в контекст безопасности Spring Security (SecurityContextHolder).
 */

@Component
public class JwtAuthenticationFilter
        extends // Расширяем OncePerRequestFilter для обработки каждого запроса
        OncePerRequestFilter //  Класс фильтра Spring Security, который гарантирует, что фильтр выполняется только один раз для каждого запроса.
{
    private final JwtHandler jwtTokenHandler; // Обработчик JWT-токенов
    private final UserDetailsService userDetailsService;// Сервис для загрузки информации о пользователе

    @Autowired
    public JwtAuthenticationFilter(
            JwtHandler jwtTokenHandler,
            UserDetailsService userDetailsService
    ) {
        this.jwtTokenHandler = jwtTokenHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(  // Переопределяем метод для обработки запроса
                                      HttpServletRequest request,// HTTP-запрос
                                      HttpServletResponse response, // HTTP-ответ
                                      FilterChain filterChain// Цепочка фильтров
    ) throws ServletException, IOException {
        String authHeader = this.parseJwt(request);// Извлекаем JWT-токен из заголовка Authorization
        if(
                Objects.nonNull(authHeader) &&// Проверяем, что токен не пустой
                        jwtTokenHandler.tokenValidator(authHeader) // Метод валидации JWT-токена.
        ){
            String userName = this.jwtTokenHandler.getUserLoginFromToken(authHeader); // Метод получения имени пользователя из JWT-токена.
            UserDetails user = this.userDetailsService.loadUserByUsername(userName); // Метод поиска пользователя в базе данных по имени пользователя.
            UsernamePasswordAuthenticationToken authenticationToken =  // Создать объект аутентификации с информацией о пользователе
                    UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities()); // Объект аутентификации, содержащий информацию о пользователе.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Метод, устанавливающий аутентификацию в контекст безопасности.
        }

        filterChain.doFilter(request, response);
// Передать управление следующему фильтру в цепочке

    }

    // Метод для извлечения JWT-токена из заголовка Authorization
    private String parseJwt(HttpServletRequest request){  //  Класс фильтра Spring Security, который гарантирует, что фильтр выполняется только один раз для каждого запроса.
        final String authHeader = request.getHeader("Authorization");// Получаем значение заголовка Authorization

        if(Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")){// Проверяем наличие префикса "Bearer "
            return authHeader.substring(7); // Возвращаем токен без префикса
        }
        return authHeader; // Возвращаем null, если токен не найден. По хорошему лучше выкинуть исключение
    }
}
