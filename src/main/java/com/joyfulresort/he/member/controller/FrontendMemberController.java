package com.joyfulresort.he.member.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.he.javamail.model.MaileService;
import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrder;
import com.joyfulresort.reserveorder.model.ResVO;
import com.joyfulresort.so.activityorder.model.ActivityOrderVO;

@Controller
@RequestMapping("/joyfulresort/member")
public class FrontendMemberController {

	@Autowired
	private MaileService mail;

	@Autowired
	private MemberService memSvc;

	@Autowired
	private StringRedisTemplate redis;

	// 會員資料頁面
	@GetMapping("/memberinfo")
	public String memberInfo(HttpSession session, Model model) {
		// 會員個人資料
		Integer memberID = Integer.valueOf((String) session.getAttribute("memberID")); // 取得session內的值
		MemberVO mem = memSvc.getOneMember(memberID); // 查找會員資料
		model.addAttribute("memberData", mem); // 轉交

		// 會員住宿訂單資料
		List<RoomOrder> userRoomOrder = memSvc.findMemberRoomOrder(memberID); // 查找住宿訂單
		model.addAttribute("memberRoomOrder", userRoomOrder);// 轉交

		// 會員活動訂單
		List<ActivityOrderVO> memberActivityOrder = memSvc.findActivityOrderByMemberId(memberID);// 查找活動訂單
		model.addAttribute("memberActivityOrder", memberActivityOrder);// 轉交

		// 會員會議廳訂單
		List<MeetingRoomOrder> memberMeetingRoomOrder = memSvc.findMeetingRoomOrderByMemberId(memberID);
		model.addAttribute("memberMeetingRoomOrder", memberMeetingRoomOrder);// 轉交

		// 會員餐廳訂單
		List<ResVO> memberReserveOrder = memSvc.findmemberReserveOrderByMemberId(memberID);
		model.addAttribute("memberReserveOrder", memberReserveOrder);// 轉交

		return "front-end/member/memberinfo";
	}

	// 註冊頁面傳來的ajax請求 對有唯一值的欄位進行檢查
	@PostMapping("/Ajax")
	public void ajax(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");

		String inputColumn = req.getParameter("inputColumn");
//		System.out.println(inputColumn);

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

//		System.out.println(img[0].isEmpty());

		if (!img[0].isEmpty()) {
//			System.out.println("img不為空");
			for (MultipartFile user : img) {
				byte[] buf = user.getBytes();
				memSvc.upDataByImg(ID, buf);
			}
			// 修改資料
			MemberVO upUserData = memSvc.upUserData(ID, inputName, inputEmail, inputPhone, inputAddrsee, inputBirthday);
			// 轉交
			model.addAttribute("memberData", upUserData);
			return "redirect:/joyfulresort/member/memberinfo?Redirect=UpData";

		} else {
//			System.out.println("img為空");
			// 修改資料
			MemberVO upUserData = memSvc.upUserData(ID, inputName, inputEmail, inputPhone, inputAddrsee, inputBirthday);
			// 轉交
			model.addAttribute("memberData", upUserData);
			return "redirect:/joyfulresort/member/memberinfo?Redirect=UpData";
		}
	}

	// 在註冊頁面 點擊註冊按鈕
	@PostMapping("/Register")
	public String userRepository(HttpServletRequest req, HttpServletResponse res, Model model, HttpSession session) {
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
		session.setAttribute("memberID", String.valueOf(newMember.getMemberId()));// 帳號密碼正確 存入Session 紀錄登入狀態

		Cookie cookie = new Cookie("LogInState", "200"); // 寫入Cookie 紀錄登入狀態 給預覽器判斷
		cookie.setMaxAge(3600); // 設定 cookie 存活時間 單位為秒
		cookie.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
		Cookie id = new Cookie("MemberID", String.valueOf(newMember.getMemberId()));
		id.setMaxAge(3600);
		id.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
		res.addCookie(id);
		res.addCookie(cookie);

		return "redirect:/joyfulresort/member/memberinfo?Redirect=Repository";
	}

	// 檢查驗證碼(寄送驗證信)
	@PostMapping("/checkAuthCode")
	@ResponseBody
	public void checkAuthCode(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		// 取得用戶輸入驗證碼
		String inputAuthCode = req.getParameter("inputAuthCode");
//		System.out.println("用戶輸入:"+inputAuthCode);
		// 取得用戶ID
		String id = req.getParameter("MemberID");
		MemberVO member = memSvc.getOneMember(Integer.valueOf(id));
//		System.out.println("ID:"+id);
		// 取得Redis內驗證碼
		String authCode = redis.opsForValue().get("memberCaptcha");
//		System.out.println("redis資料:"+authCode);

		JSONObject obj = new JSONObject();
		if (authCode == null) { // Redis 值為空 超時
			obj.put("checkAuthCode", "404");
			res.getWriter().print(obj);
		} else if (inputAuthCode.equals(authCode)) { // 輸入正確

			// 產生驗證碼
			RedisController AuthCode = new RedisController();
			String emailVerifCode = AuthCode.returnAuthCode();

//			mail.sendSimpleMail();
			redis.opsForValue().set("emailVerif", emailVerifCode, 3, TimeUnit.MINUTES);
			mail.sendMimeMail(emailVerifCode, member.getMemberEmail());

//			memSvc.memberStateUpData(Integer.valueOf(id)); // 檢查會員狀態並修改狀態

			obj.put("checkAuthCode", "200");
			res.getWriter().print(obj);
		} else { // 輸入錯誤
			obj.put("checkAuthCode", "400");
			res.getWriter().print(obj);
		}
		return;
	}

	// 修改密碼
	@PostMapping("/passwordRevise")
	@ResponseBody
	public boolean revisePassword(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("application/json; charset=UTF-8");
		// 取得用戶輸入資料
		String ID = req.getParameter("MemberID");
		String pw_1 = req.getParameter("password_1");
		String pw_2 = req.getParameter("password_2");
		String pw_3 = req.getParameter("password_3");

//		System.out.printf("用戶ID: %s%n密碼: %s%n新密碼: %s%n再輸入: %s%n", ID, pw_1, pw_2, pw_3);

		MemberVO mem = memSvc.getOneMember(Integer.valueOf(ID));
		String password = mem.getMemberPassword();
//		System.out.println("資料庫密碼: "+password);
//		System.out.println("--------------------------");

		if (password.equals(pw_1)) {
			if (pw_2.equals(pw_3)) {
//				System.out.println("密碼正確 pw_2,pw_3輸入一致");
				// 修改會員密碼
				mem.setMemberPassword(pw_2);
				memSvc.changePassword(mem);

				return true;
			}
//			System.out.println("密碼正確 pw_2,pw_3輸入不一致");
			return false;
		} else {
//			System.out.println("密碼錯誤");
			return false;
		}

	}

	// 用戶取消訂單
	@PostMapping("/CancelOrder")
	@ResponseBody
	public void CancelOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		JSONObject obj = new JSONObject();
		
		String CancelOrder = req.getParameter("CancelOrder");
//		System.out.println(CancelOrder);
		switch (CancelOrder) {
		case "activity":
			Integer activityOrderID = Integer.valueOf(req.getParameter("activityOrderID"));
//			System.out.println(OrderID);
			// 取消活動訂單
			memSvc.activityCancelOrder(activityOrderID);

			

			obj.put("CancelOrder", true);
			res.getWriter().print(obj);

			break;
		case "meetingRoom":
			Integer MeetingRoomOrderID = Integer.valueOf(req.getParameter("MeetingRoomOrderID"));
//			System.out.println(MeetingRoomOrderID);

			// 取消會議廳訂單
			memSvc.meetingRoomCancelOrder(MeetingRoomOrderID);

			obj.put("CancelOrder", true);
			res.getWriter().print(obj);
			
			break;
		case "ReserveOrder":
			Integer ReserveOrderID = Integer.valueOf(req.getParameter("ReserveOrderID"));
//			System.out.println(ReserveOrderID);

			// 取消餐廳訂單
			memSvc.ReserveCancelOrder(ReserveOrderID);
			
			obj.put("CancelOrder", true);
			res.getWriter().print(obj);
			
			break;
		case "RoomOrder":
			Calendar nowTimeCalendar = new GregorianCalendar();// Date 日期加減需要使用 Calendar抽象類 實作GregorianCalendar
			Calendar calendar = new GregorianCalendar();

			Integer RoomOrderID = Integer.valueOf(req.getParameter("RoomOrderID"));
			Date nowTime = Date.valueOf(req.getParameter("nowTime"));
			nowTimeCalendar.setTime(nowTime);
//			System.out.println("訂單取消的時間:"+nowTimeCalendar.getTime());

			// 取得資料庫內資料
			RoomOrder memberRoomOrder = memSvc.getRoomOrder(RoomOrderID);
			Date checkin = memberRoomOrder.getCheckInDate();
//			System.out.println(checkin);

			// 使用 Calendar 操作時間
			calendar.setTime(checkin);
//			System.out.println("原本的預計入住日期:" + calendar.getTime());
			calendar.add(calendar.DATE, -3);
//			System.out.println("三天前:" + calendar.getTime());

			// 比較時間 當calendar(預計入住的3天前) 比 nowTime(取消訂單的時間) 小時回傳true
//			System.out.println(calendar.before(nowTimeCalendar));
			// 取消住宿訂單
			if (calendar.before(nowTimeCalendar)) { // 不退款
				memSvc.RoomCancelOrder(RoomOrderID, (byte) 0);
				
				obj.put("CancelOrder", true);
				obj.put("refund", true);
				res.getWriter().print(obj);
				
				
			} else { // 退款
				memSvc.RoomCancelOrder(RoomOrderID, (byte) 1);
				
				obj.put("CancelOrder", true);
				obj.put("refund", false);
				res.getWriter().print(obj);
			}

			
			break;
		}
	}

	// 忘記密碼
	@PostMapping("/forgetPassword")
	public void forgetPassword(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		// 取得資料
		String inuptMail = req.getParameter("inputEmail");
		String inputAuthCode = req.getParameter("authCode");
//		System.out.println(inuptMail);
//		System.out.println(inputAuthCode);

		// 檢查資料庫信箱
		Boolean email = memSvc.checkEmail(inuptMail);
//		System.out.println("0有無信箱"+email);
		JSONObject obj = new JSONObject();
//		System.out.println(email);
		// 檢查有無信箱
		if (email) {
			// 檢查驗證碼 取得Redis內驗證碼
			String authCode = redis.opsForValue().get("memberCaptcha");
//			System.out.println("1驗證碼為"+authCode);
			if (authCode == null) {
//				System.out.println("2驗證碼為空");
				obj.put("error", "驗證碼過期 請重新取得");
				res.getWriter().print(obj);
				
			} else if (inputAuthCode.equals(authCode)) { // 有信箱 驗證碼輸入正確
//				System.out.println("3驗證碼輸入正確");
				// 產生新密碼
				RedisController AuthCode = new RedisController();
				String authCode2 = AuthCode.returnAuthCode();
				// 修改密碼
				MemberVO member = memSvc.findMemberByMail(inuptMail, authCode2);

				String member_name = member.getMemberName();
				String subject = "JoyfulResort-密碼更改通知";
				String messageText = "Hello! " + member_name + "您的密碼已修改 請謹記此密碼: " + authCode2 + "\n" + " (已經啟用)";

				// 寄送新密碼
				mail.sendSimpleMail(inuptMail, subject, messageText);

				obj.put("error", "true");
				res.getWriter().print(obj);

			} else {
//				System.out.println("4驗證碼輸入錯誤");
				obj.remove("error");
				obj.put("error", "驗證碼錯誤");
				res.getWriter().print(obj);
			}

		} else {
//			System.out.println("5無此信箱");
			obj.put("error", "無此信箱");
			res.getWriter().print(obj);

		}

	}

	// 信箱驗證
	@GetMapping("/emailVerify")
	public String emailVerify(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		String authCode = req.getParameter("AuthCode");
		String memberEmail = req.getParameter("memberEmail");
//		System.out.println(authCode);
//		System.out.println(memberEmail);

		// 檢查驗證碼 取得Redis內驗證碼
		String redisAuthCode = redis.opsForValue().get("emailVerif");
//		System.out.println(redisAuthCode);
		if (redisAuthCode == null) {
//			System.out.println("驗證碼已過期");
			return "front-end/member/memberExpired";

		} else if (redisAuthCode.equals(authCode)) {
			MemberVO mem = memSvc.findMemberByMail(memberEmail);

			memSvc.memberStateUpData(mem.getMemberId()); // 檢查會員狀態並修改狀態
			session.setAttribute("memberID", mem.getMemberId());

			Cookie cookie = new Cookie("LogInState", "200"); // 寫入Cookie 紀錄登入狀態 給預覽器判斷
			cookie.setMaxAge(3600); // 設定 cookie 存活時間 單位為秒
			cookie.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
			Cookie id = new Cookie("MemberID", String.valueOf(mem.getMemberId()));
			id.setMaxAge(3600);
			id.setPath("/"); // 確保 Cookie 在整個應用程式都可使用
			res.addCookie(id);
			res.addCookie(cookie);

			return "redirect:/joyfulresort/member/memberinfo?Redirect=emailVerify";

		}

//		錯誤
		return "redirect:/joyfulresort/member/memberinfo?Redirect=emailVerifyError";
	}

}
