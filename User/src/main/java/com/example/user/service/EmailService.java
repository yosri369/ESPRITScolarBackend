package com.example.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendStudentAccountInfoEmail(String to, String systemEmail, String password, String registrationNumber) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your ESPRITScolar Account Is Ready");

            String content = """
                <p>Hello,</p>
                <p>Your ESPRITScolar account has been created. Here are your login credentials:</p>
                <ul>
                    <li><strong>Email:</strong> %s</li>
                    <li><strong>Password:</strong> %s</li>
                    <li><strong>Registration Number:</strong> %s</li>
                </ul>
                <p>Please change your password after first login.</p>
                <p>Best regards,<br/>ESPRITScolar Team</p>
            """.formatted(systemEmail, password, registrationNumber);

            helper.setText(content, true);
            mailSender.send(message);

            System.out.println("✅ Email sent to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to send email to " + to);
        }
    }

    public void sendTeacherAccountInfoEmail(String to, String universityEmail, String username, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to); // recipient (teacher's personal/form email)
            helper.setSubject("Your ESPRITScolar Teacher Account Is Ready");

            String content = """
            <p>Hello,</p>
            <p>Your ESPRITScolar teacher account has been created. Here are your login credentials:</p>
            <ul>
                <li><strong>Username (Registration Number):</strong> %s</li>
                <li><strong>Password:</strong> %s</li>
                <li><strong>Email:</strong> %s</li>
            </ul>
            <p>Please change your password after your first login.</p>
            <p>Best regards,<br/>ESPRITScolar Team</p>
        """.formatted(username, password, universityEmail);

            helper.setText(content, true);
            mailSender.send(message);
            System.out.println("✅ Email sent to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to send email to " + to);
        }
    }
}
