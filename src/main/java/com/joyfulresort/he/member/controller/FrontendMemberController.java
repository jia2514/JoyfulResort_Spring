package com.joyfulresort.he.member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;

@Controller
@RequestMapping("/frontend/member")
public class FrontendMemberController {

	@Autowired
	MemberService memSvc;

	// 登入頁面
	@GetMapping("/memberLogin.html")
	public String memberLogin() {
		return "frontend/member/memberLogin";
	}

	// 登入頁面點擊註冊 轉跳至註冊頁面
	@GetMapping("/memberRegister")
	public String Repository() {
		return "frontend/member/memberRegister";
	}

	// 會員資料頁面
	@GetMapping("/memberinfo.html")
	public String memberInfo(HttpSession session, Model model) {
		Integer userId = (Integer) session.getAttribute("memberID"); // 取得session內的值

		MemberVO mem = memSvc.getOneMember(userId); // 查找會員資料
		model.addAttribute("memberData", mem); // 轉交
		return "frontend/member/memberinfo";
	}

	// 註冊頁面傳來的ajax請求 對有唯一值的欄位進行檢查
	@PostMapping("/Ajax")
	public void ajax(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json; charset=UTF-8");

		String inputColumn = req.getParameter("inputColumn");
		System.out.println(inputColumn);

		// 檢查輸入欄位
		switch (inputColumn) {
		case "Account":
			String inputAccount = req.getParameter("inputAccount"); // 取得UESR輸入資料
			Boolean acc = memSvc.checkAccount(inputAccount); // 檢查資料庫資料 判斷有無重複

			JSONObject objAccount = new JSONObject(); // 使用JSON回傳結果
			if (acc) {
				objAccount.put("inputAccount", true);
				res.getWriter().print(objAccount);
			} else {
				objAccount.put("inputAccount", false);
				res.getWriter().print(objAccount);
			}
			break;
		case "Phone":
			String inputPhone = req.getParameter("inputPhone"); // 取得UESR輸入資料
			Boolean phone = memSvc.checkPhone(inputPhone); // 檢查資料庫資料 判斷有無重複

			JSONObject objPhone = new JSONObject(); // 使用JSON回傳結果
			if (phone) {
				objPhone.put("inputPhone", true);
				res.getWriter().print(objPhone);
			} else {
				objPhone.put("inputPhone", false);
				res.getWriter().print(objPhone);
			}
			break;
		case "Email":
			String inputEmail = req.getParameter("inputEmail"); // 取得UESR輸入資料
			Boolean email = memSvc.checkEmail(inputEmail); // 檢查資料庫資料 判斷有無重複

			JSONObject objEmail = new JSONObject(); // 使用JSON回傳結果
			if (email) {
				objEmail.put("inputEmail", true);
				res.getWriter().print(objEmail);
			} else {
				objEmail.put("inputEmail", false);
				res.getWriter().print(objEmail);
			}
			break;
		}

	}

	// 用戶登入
	@PostMapping("/Login")
	public String Login(HttpServletRequest req, HttpSession session, Model model) {
		// 接收資料
		String userAccount = req.getParameter("userAccount");
		String userPassword = req.getParameter("userPassword");

		// 取得資料庫的USER資料
		MemberVO mem = memSvc.getUserData(userAccount);

		// 判斷USER輸入的帳號密碼是否正確
		if (mem == null) {
			System.out.println("查無帳號"); // 查無帳號
			model.addAttribute("errorMessage", "帳號或密碼錯誤");
		} else if (!userPassword.equals(mem.getMemberPassword())) {
			System.out.println("密碼錯誤"); // 密碼錯誤
			model.addAttribute("errorMessage", "帳號或密碼錯誤");
		} else {
			System.out.println("密碼正確"); // 密碼正確
			session.setAttribute("memberID", mem.getMemberId());// 帳號密碼正確 存入Session 紀錄登入狀態

			return "redirect:/frontend/member/memberinfo.html"; // 轉向至會員個人資料
		}
		return "frontend/member/memberLogin"; // 回到登入頁面
	}

	// USER修改資料
	@PostMapping("/userUpData")
	public String userUpData(HttpServletRequest req, HttpSession session, Model model,
			@RequestParam("memberImg") MultipartFile[] img) throws IOException {
		// 取得USER輸入的值
		Object MemberID = session.getAttribute("memberID");
		String inputName = req.getParameter("memberName");
		String inputEmail = req.getParameter("memberEmail");
		String inputPhone = req.getParameter("memberPhone");
		String inputAddrsee = req.getParameter("memberAddress");
		String inputBirthday = req.getParameter("memberBirthday");
		String ID = String.valueOf(MemberID);
		
		System.out.println(img[0].isEmpty());
		
		if (!img[0].isEmpty()) {
			System.out.println("img不為空");
			for (MultipartFile user : img) {
				byte[] buf = user.getBytes();
				memSvc.upDataByImg(ID, buf);
			}
			// 修改資料
			MemberVO upUserData = memSvc.upUserData(ID, inputName, inputEmail, inputPhone, inputAddrsee, inputBirthday);
			// 轉交
			model.addAttribute("memberData", upUserData);
			return "frontend/member/memberinfo.html";

		} else {			
			System.out.println("img為空");
			// 修改資料
			MemberVO upUserData = memSvc.upUserData(ID, inputName, inputEmail, inputPhone, inputAddrsee, inputBirthday);
			// 轉交
			model.addAttribute("memberData", upUserData);
			return "frontend/member/memberinfo.html";
		}
	}

	// 在註冊頁面 點擊註冊按鈕
	@PostMapping("/Register")
	public String userRepository(HttpServletRequest req, Model model, HttpSession session) {
//		System.out.println("!!註冊!!");
		// 取得USER輸入的值
		String userName = req.getParameter("userName");
		String userAccount = req.getParameter("userAccount");
		String userPassword = req.getParameter("userPassword");
		String userPhone = req.getParameter("userPhone");
		String userEmail = req.getParameter("userEmail");
		String userAddress = req.getParameter("userAddress");
		Date userBirthday = Date.valueOf(req.getParameter("userBirthday"));
		Integer userGender = Integer.valueOf(req.getParameter("userGender"));

//		System.out.println(userName);
//		System.out.println(userAccount);
//		System.out.println(userPassword);
//		System.out.println(userPhone);
//		System.out.println(userEmail);
//		System.out.println(userAddress);
//		System.out.println(userBirthday);
//		System.out.println(userGender);
		// 新增帳號
		MemberVO newMember = memSvc.newMember(userName, userAccount, userPassword, userEmail, userPhone, userAddress,
				userGender, userBirthday);
		// 轉交 寫入session保存登入狀態
		model.addAttribute("memberData", newMember);
//		System.out.println(newMember.getMemberId());
		session.setAttribute("memberID", newMember.getMemberId());// 帳號密碼正確 存入Session 紀錄登入狀態

		return "frontend/member/memberinfo.html";
	}

}
