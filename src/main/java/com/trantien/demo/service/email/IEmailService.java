package com.trantien.demo.service.email;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

public interface IEmailService {
    public void sendEmail(String to, String subject, String body) throws MessagingException;
}
