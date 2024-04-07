package com.company.project.mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl{

    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender mailSender){
      this.emailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);

      emailSender.send(message);
  }
}
