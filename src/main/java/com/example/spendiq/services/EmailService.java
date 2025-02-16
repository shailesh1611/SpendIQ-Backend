package com.example.spendiq.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.sender}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String to, String otp) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("Your OTP code");
            helper.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");
            mailSender.send(message);
            log.info("OTP email sent to: {}", to);
        }
        catch(MessagingException e) {
            log.error("Email Failed : {}", e.getMessage());
            throw new MessagingException("Something went wrong, Unable to send Otp.");
        }
    }
}
