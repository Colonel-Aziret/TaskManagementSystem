package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.utils.email.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
