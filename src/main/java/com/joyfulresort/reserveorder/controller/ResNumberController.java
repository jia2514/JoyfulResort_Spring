package com.joyfulresort.reserveorder.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reservesession.model.RessionService;

@Controller
@Validated
public class ResNumberController {

	@Autowired
	ResService resSvc;

	@Autowired
	RessionService ressionSvc;

	private Integer number;
	private String message;
	private String message101;
	private String message102;
	private Integer maxpart;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage101() {
		return message101;
	}

	public void setMessage101(String message101) {
		this.message101 = message101;
	}

	public String getMessage102() {
		return message102;
	}

	public void setMessage102(String message102) {
		this.message102 = message102;
	}

	public Integer getMaxpart() {
		return maxpart;
	}

	public void setMaxpart(Integer maxpart) {
		this.maxpart = maxpart;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public ResNumberController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@PostMapping("/total1")
	@ResponseBody
	public ResNumberController total(
			@RequestParam(value = "bookingDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime bookingDate2,
			@RequestParam(value = "bookingDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate bookingDate,
			ModelMap model) {

		if (bookingDate2 == null) {
			bookingDate2 = LocalDateTime.of(2024, 1, 1, 1, 1);
		}

		ResNumberController response = new ResNumberController();
		Integer hour = 1;  //初始化 免得網頁重整抓到舊資料
		number = 1;
		hour = bookingDate2.getHour();
//		System.out.println(bookingDate2);
//		System.out.println(hour);

		maxpart = ressionSvc.getMaxPartById(101);

		if (hour >= 17 && hour < 22) {
			number = resSvc.countNumber102(bookingDate);
			message102 = "當日晚餐時段已客滿<br>請選擇白天時段或其他日期";

		} else if (hour >= 10 && hour < 15) {
			number = resSvc.countNumber101(bookingDate);
			message101 = "當日午餐時段已客滿<br>請選擇晚上時段或其他日期";
		} else if (hour == 0) {
			number = 0;
			message = "請選擇時間";
		}
		if (number == null) {
			number = -1;

		}
		response.setMessage(message);
		response.setMessage101(message101);
		response.setMessage102(message102);
		response.setNumber(number);
		response.setMaxpart(maxpart);
//		System.out.println(hour);

		return response;
	}

}
