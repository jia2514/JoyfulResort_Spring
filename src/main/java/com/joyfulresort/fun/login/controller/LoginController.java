package com.joyfulresort.fun.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//處理登錄請求的控制器方法
@Controller
public class LoginController {

	@PostMapping("/login")
	public String login() {
		// 登錄成功後重定向到主頁面
		return "back-end/main_page";
	}

	 @GetMapping("/loginfailed")
	public String loginfailed(Model model) {
		// 登入驗證失敗回到登入畫面
		model.addAttribute("loginError", "帳號或密碼錯誤，請重新輸入。");
		return "back-end/backend_login";
	}
	 
	 
	 
	 @GetMapping("/loginfailedNoAcess")
	public String loginfailedNoAcess(Model model) {
		// 登入驗證失敗回到登入畫面
		model.addAttribute("loginError", "該員工無登入權限。");
		return "back-end/backend_login";
	}
	 
	 
	 @GetMapping("/loginfailedCaptcha")
		public String loginfailedCaptcha(Model model) {
			// 登入驗證失敗回到登入畫面
			model.addAttribute("loginError", "驗證碼錯誤，請重新輸入");
			return "back-end/backend_login";
		}

}
