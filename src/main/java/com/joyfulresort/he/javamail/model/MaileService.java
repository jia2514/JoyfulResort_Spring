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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service("MaileService")
public class MaileService {

	@Autowired
	@Qualifier("memberJavaMailSender")
	private JavaMailSender sender;
	

	private String subject = "郵件標題:Spring boot mail test";
	private String content = "郵件內容: 測試郵件";
	// 收件地址
	private String to = "dh91009@gmail.com";
	// 發送地址
	private String from = "童樂渡假村 <dh91009@gmail.com>";

	// 純文字郵件(test)
	public void sendSimpleMail(String to, String subject, String messageText) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setSubject(subject);
		mail.setText(messageText);
		mail.setTo(to);
		mail.setFrom(from);
		sender.send(mail);
		System.out.println("發送成功!!");

	}

	// 複雜郵件 (會員驗證信)
	public void sendMimeMail(String authCode, String memberEmail) {
		MimeMessage mail = sender.createMimeMessage();
		

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mail, true ,"UTF-8");
			helper.setSubject("JoyfulResort-會員驗證信");
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

}
