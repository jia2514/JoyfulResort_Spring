package com.joyfulresort.he.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;

@Controller
@RequestMapping("/joyfulresort")
public class MemberLoginController {

	@Autowired
	MemberService memSvc;

	// 登入頁面點擊註冊 轉跳至註冊頁面
	@GetMapping("/memberRegister")
	public String Repository(HttpSession session) {
		Object memberID = session.getAttribute("memberID");
		// 判斷用戶有無登入
		if (memberID == null) { // 沒有-->註冊頁面
			return "front-end/member/memberRegister";
		} else { // 有-->個人頁面
			return "redirect:/joyfulresort/member/memberinfo";
		}

	}

	// 用戶登入
	@PostMapping("/Login")
	public void Login(HttpServletRequest req, HttpServletResponse res, HttpSession session, Model model)
			throws ServletException, IOException {
		res.setContentType("application/json; charset=UTF-8");
		// 接收資料
		String userAccount = req.getParameter("userAccount");
		String userPassword = req.getParameter("userPassword");

//		System.out.println(userAccount);
//		System.out.println(userPassword);
		// 取得資料庫的USER資料
		MemberVO mem = memSvc.getUserData(userAccount);

		JSONObject obj = new JSONObject();
		// 判斷USER輸入的帳號密碼是否正確
		if (mem == null) {
//			System.out.println("查無帳號"); // 查無帳號
			obj.put("State", false);
			res.getWriter().print(obj); // 轉交ajax請求
			return;
		} else if (mem.getMemberState() == 2) { //帳號停權
			obj.put("State", false);
			obj.put("AccountState", "2");
			res.getWriter().print(obj);
			
			return;
		} else if (!userPassword.equals(mem.getMemberPassword())) {
//			System.out.println("密碼錯誤"); // 密碼錯誤
			obj.put("State", false);
			res.getWriter().print(obj); // 轉交ajax請求
			return;
		} else {
//			System.out.println("密碼正確"); // 密碼正確
			session.setAttribute("memberID", mem.getMemberId());// 帳號密碼正確 存入Session 紀錄登入狀態

			Cookie cookie = new Cookie("LogInState", "200"); // 寫入Cookie 紀錄登入狀態 給預覽器判斷
			cookie.setMaxAge(3600); // 設定 cookie 存活時間 單位為秒
			cookie.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
			Cookie id = new Cookie("MemberID", String.valueOf(mem.getMemberId()));
			id.setMaxAge(3600);
			id.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
			res.addCookie(id);
			res.addCookie(cookie);

			obj.put("State", true);
			res.getWriter().print(obj); // 轉交ajax請求
			return;
		}
	}

	// 用戶登出
	@GetMapping("/LogOut")
	public String LogOut(HttpSession session, HttpServletResponse res, HttpServletRequest req) {

		String memberID = String.valueOf(session.getAttribute("memberID"));
		// 移除紀錄登入狀態的session
		session.removeAttribute("memberID");

		// 移除登入狀態的Cookie
		Cookie cookie = new Cookie("LogInState", "200");
		cookie.setMaxAge(0); // 設定 cookie 存活時間 0-->立刻失效
		cookie.setPath("/");
		res.addCookie(cookie);

		Cookie id = new Cookie("MemberID", memberID);
		id.setMaxAge(0); // 設定 cookie 存活時間 0-->立刻失效
		id.setPath("/");
		res.addCookie(id);

		return "redirect:/";
	}

}
