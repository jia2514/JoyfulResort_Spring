package com.joyfulresort.he.member.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.he.member.model.MemberService;

@Controller
@RequestMapping("/member")
public class DBReaderController{
	
	@Autowired
	MemberService memSvc;
	
	@GetMapping("/DBReader")
	public void dBReader(@RequestParam("memberID") String ID, HttpServletRequest req, HttpServletResponse res) throws IOException{
		
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		
		out.write(memSvc.getOneMember(Integer.valueOf(ID)).getMemberImg());		
	}
	
	
}
