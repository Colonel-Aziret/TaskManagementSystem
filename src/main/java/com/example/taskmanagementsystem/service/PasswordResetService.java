package com.example.taskmanagementsystem.service;

public interface PasswordResetService {
    void sendResetPasswordEmail(String email);
    void resetPassword(String token, String newPassword);
}
