package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.entity.PasswordResetToken;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.repository.PasswordResetTokenRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.service.EmailService;
import com.example.taskmanagementsystem.service.PasswordResetService;
import com.example.taskmanagementsystem.utils.email.EmailDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Transactional
    public void sendResetPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Сначала удаляем старые токены в отдельной транзакции
        removeOldTokens(user);

        // Теперь создаем новый токен
        createNewTokenAndSendEmail(user);
    }

    @Transactional
    public void removeOldTokens(User user) {
        log.info("delete user by id: {}", user.getId());
        passwordResetTokenRepository.deleteByUser(user);
        passwordResetTokenRepository.flush();  // Принудительно выполняем удаление до создания нового токена
    }

    @Transactional
    public void createNewTokenAndSendEmail(User user) {
        // Генерация нового токена
        String token = UUID.randomUUID().toString();

        // Сохранение токена в базе данных
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Токен действителен 1 час
        passwordResetTokenRepository.save(resetToken);

        // Отправка email
        String message = String.format("Hello, %s! To reset your password, please copy this token below:\n%s", user.getUsername(), resetToken.getToken());

        EmailDetails details = new EmailDetails();
        details.setRecipient(user.getEmail());
        details.setSubject("Password Reset Request");
        details.setMessageBody(message);

        emailService.sendSimpleMail(details);
    }

    // Проверка токена и сброс пароля
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // Проверка срока действия токена
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        // Обновление пароля пользователя
        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        // Удаление использованного токена
        passwordResetTokenRepository.delete(resetToken);
    }
}
