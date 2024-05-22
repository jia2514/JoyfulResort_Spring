package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
    @Async
	public void sendPlainText(String userEmail, String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);//收件人信箱
		message.setSubject(subject);//信件主題
        message.setText("使用者信箱: " + userEmail + "\n" + content);//內容
		message.setFrom("JoyfulResort<xd890872680@gmail.com>");//寄件人名字
		message.setReplyTo(userEmail); 
		mailSender.send(message);
	}

}
