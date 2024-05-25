package com.joyfulresort.he.javamail.model;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("MaileService")
public class MaileService {

	@Autowired
	private JavaMailSender sender;

	private String subject = "郵件標題:Spring boot mail test";
	private String content = "郵件內容: 測試郵件";
	// 收件地址
	private String to = "dh91009@gmail.com";
	// 發送地址
	private String from = "dh91009@gmail.com";

	// 純文字郵件(test)
	public void sendSimpleMail() {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setSubject(subject);
		mail.setText(content);
		mail.setTo(to);
		mail.setFrom(from);
		sender.send(mail);
//		System.out.println("發送成功!!");

	}

	// 複雜郵件 (會員驗證信)
	public void sendMimeMail(String authCode, String memberEmail) {
		MimeMessage mail = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail);

		try {
			helper.setSubject("會員驗證信");
			helper.setText("""
					<!DOCTYPE html>
					<html lang="zh-Hant">
					<head>
					    <meta charset="UTF-8">
					    <meta name="viewport" content="width=device-width, initial-scale=1.0">
					    <title>JoyfulResort - 登入</title>
					    <style>
					        body {
					            font-family: Arial, sans-serif;
					            margin: 0;
					            padding: 0;
					            background-color: #fff; /* 設置背景為淺灰色 */
					            display: flex;
					            justify-content: center;
					            align-items: center;
					            height: 100vh;
					        }
					        .modal-body {
					            background-color: #e0e0e0;
					            padding: 2rem;
					            border-radius: 8px;
					            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
					            width: 400px;
					            text-align: center;
					        }
					        .modal-body h1 {
					            color: rgb(0, 174, 255);
					            margin-bottom: 1rem;
					            text-align: center;
					        }
					        .modal-body h4 {
					            margin-bottom: 1rem;
					            text-align: left;
					        }
					        .modal-body p {
					            margin-bottom: 2rem;
					            line-height: 1.5;
					            color: #333;
					            text-align: left;
					        }
					        .modal-body a {
					            display: inline-block;
					            width: 50%;
					            text-decoration: none;
					        }
					        .modal-body button {
					            width: 100%;
					            padding: 1rem;
					            background-color: rgb(0, 174, 255);
					            color: #fff;
					            border: none;
					            border-radius: 5px;
					            cursor: pointer;
					            font-size: 1rem;
					            transition: background-color 0.3s ease;
					        }
					        .modal-body button:hover {
					            background-color: rgb(0, 150, 220);
					        }
					    </style>
					</head>
					<body>
					    <div class="modal-body">
					        <h1><b>JoyfulResort</b></h1>
					        <h4><b>親愛的會員您好:</b></h4>
					        <p>請點擊驗證按鈕進行帳號驗證。<br>注意驗證有效時間為3分鐘，逾期請重新申請。</p>
					        <div style="margin-top: 35px;">
					            <a href='http://localhost:8080/joyfulresort/member/emailVerify?AuthCode=""" + authCode + """
					            		&memberEmail=""" + memberEmail + """ 
					            '>
					                <button type="button">驗證</button>
					            </a>
					        </div>
					    </div>
					</body>
					</html>
					""", true);
			helper.setTo(to);
			helper.setFrom(from);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sender.send(mail);
//		System.out.println("Mime郵件發送成功");
	}

	public void sendMail(String to, String subject, String messageText) {

		try {
			// 設定使用SSL連線至 Gmail smtp Server
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			final String myGmail = "dh91009@gmail.com";
			final String myGmail_password = "qdqsrhsgvmkzyzyf";
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(myGmail, myGmail_password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myGmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// 設定信中的主旨
			message.setSubject(subject);
			// 設定信中的內容
			message.setText(messageText);

			Transport.send(message);
			System.out.println("傳送成功!");
		} catch (MessagingException e) {
			System.out.println("傳送失敗!");
			e.printStackTrace();
		}
	}

}
