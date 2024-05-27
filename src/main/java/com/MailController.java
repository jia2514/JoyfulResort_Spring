package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MailController {

	@Autowired
	private MailService mailService;

	@PostMapping("/sendMail")
	public ResponseEntity<?> sendMail(@RequestBody ContactForm form) {
		try {
			String to = "xd890872680@gmail.com"; //要拿來收信的信箱 (商家)
			String content = "姓名: " + form.getName() + "\n\n內容:\n" + form.getMessage();
			mailService.sendPlainText(
					form.getEmail(),
					to,
					form.getSubject(),
					content
		);
													//成功訊息以及重導網頁
			return ResponseEntity.ok().body("{\"success\": true,\"redirect\": \"/\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("{\"success\": false}");
		}
	}
}

class ContactForm {
	private String name;
	private String email;
	private String subject;
	private String message;

	// Getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}