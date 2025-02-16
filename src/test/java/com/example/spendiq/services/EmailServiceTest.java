package com.example.spendiq.services;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void testSendOtp() throws MessagingException {
        String otp = "394292";
        String to = "shaileshsingh8077@gmail.com";
        emailService.sendOtp(to,otp);
    }
}
