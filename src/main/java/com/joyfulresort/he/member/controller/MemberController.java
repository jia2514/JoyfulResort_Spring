package com.joyfulresort.he.member.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;

@Controller
@RequestMapping("/backend/member")
public class MemberController {

	@Autowired
	MemberService memSvc;

	// 進入頁面後給AllMember資料
	@GetMapping("/blankMember.html")
	public String blankMember(Model model) {
		List<MemberVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);
		return "backend/member/blankMember";
	}

	// 點擊修改按鈕發送Ajax請求
	@PostMapping("/Ajax")
	@ResponseBody()
	public MemberVO getAjax(HttpServletRequest req, Model model) {

		Integer id = Integer.valueOf(req.getParameter("memberID")); // 取得Ajax資料
		MemberVO member = memSvc.memberStateFindById(id); // 檢查會員狀態並修改狀態
//		List<MemberVO> list = memSvc.getAll();
//		model.addAttribute("memListData", list);
//		System.out.println("修改後的狀態:"+memberState.getMemberState());

		return member;
	}

}
