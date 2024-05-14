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

@Controller
@Validated
public class ResNumberController {

	@Autowired
	ResService resSvc;

	private Integer number;
	private String message;

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResNumberController() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getNumber() {
		return number;
	}

	public String getMessage() {
		return message;
	}

	@PostMapping("/total1")
	@ResponseBody
	public ResNumberController total(
			@RequestParam(value = "bookingDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime bookingDate2,
			@RequestParam(value = "bookingDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDate bookingDate,
			ModelMap model) {

		if (bookingDate2 == null) {
			  bookingDate2 = LocalDateTime.of(2024, 1, 1, 0, 0); 
		}

		ResNumberController response = new ResNumberController();
		Integer hour;
		hour = bookingDate2.getHour();
	

		if (hour >= 17 && hour < 22) {
			number = resSvc.countNumber102(bookingDate);
			message = "當日晚餐時段已客滿，請選擇午餐時段(11-14)";

		} else if (hour >= 10 && hour < 15) {
			number = resSvc.countNumber101(bookingDate);
			message = "當日午餐時段已客滿，請選擇晚餐時段(17-20)";
		} else if (hour == 0) {
			number = 0;
		}

		if (number == null) {
			number = -1;

		}
		response.setMessage(message);
		response.setNumber(number);
//	System.out.println(number);
		return response;
	}

}
