package com.joyfulresort.fun.empmail.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("employeeMailService")
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(@Qualifier("backEndMailSender") JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // 使用 MimeMessageHelper 設置郵件屬性
            MimeMessageHelper helper = new MimeMessageHelper(message, true,  "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true 表示發送HTML内容
            helper.setFrom("童樂渡假村 <ceshixu297@gmail.com>"); // 設置發件人的名稱和郵箱地址
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
