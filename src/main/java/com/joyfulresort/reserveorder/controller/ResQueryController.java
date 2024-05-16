package com.joyfulresort.reserveorder.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reserveorder.model.ResVO;
import com.joyfulresort.reservesession.model.RessionService;
@Validated
@Controller
@RequestMapping("/reserve")
public class ResQueryController {

	@Autowired
	MemberService memberSvc;
	@Autowired
	RessionService ressionSvc;
	@Autowired
	ResService resSvc;

	@PostMapping("get_query")
	public String get_query(
@NotEmpty(message = "單筆搜尋欄位請勿空白")
@Digits(integer = 4, fraction = 0, message = "只能是數字,且不得超過4位數")
//@Pattern(regexp = "^$|\\d+", message = "只能是數字") 
@RequestParam(value = "reserveOrderId") String reserveOrderId, ModelMap model) {

		ResVO resVO = resSvc.getOneRes(Integer.valueOf(reserveOrderId));

		List<ResVO> list = resSvc.getAllRes();
		model.addAttribute("ResList", list);// 為配合顯示所有的表格
		model.addAttribute("ResListData", list);// 用來顯示下拉選單
		model.addAttribute("ResList", resVO);

		if (resVO == null) {	
			model.addAttribute("message", "沒有符合的資料");
//			return "back-end/reserve/reserveorder"; // 無資料是否返回顯示所有
		}

		return "back-end/reserve/reserveorder";
	}

	@PostMapping("get_many_query")
	public String get_many_query(

			@RequestParam(value = "reserveOrderDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reserveOrderDate,
			@RequestParam(value = "bookingDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate bookingDate,
			ModelMap model) {
		List<ResVO> resVO = resSvc.findByDates(reserveOrderDate, bookingDate);

		List<ResVO> list = resSvc.getAllRes();
		model.addAttribute("ResList", list);
		model.addAttribute("ResListData", list);
		if (resVO.isEmpty()) {
			model.addAttribute("message", "沒有符合的資料");
//			return "back-end/reserve/reserveorder"; //無資料是否返回顯示所有
		}

		model.addAttribute("ResList", resVO);

		return "back-end/reserve/reserveorder";
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			strBuilder.append(violation.getMessage() + "<br>");

		}

		List<ResVO> list = resSvc.getAllRes();
		model.addAttribute("ResListData", list);
//		model.addAttribute("ResList", list);// 錯誤時顯示所有清單
		String message = strBuilder.toString();
		return new ModelAndView("/back-end/reserve/reserveorder", "message", message);

	}

}
