package com.joyfulresort.yu.roomtype.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.yu.room.model.RoomService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhotoService;

@Controller
@RequestMapping("/joyfulresort/roomtype")
public class FrontendRoomTypeController {

	@Autowired
	RoomTypeService roomTypeSvc;

	@Autowired
	RoomService roomSvc;
	
	@Autowired
	RoomTypePhotoService roomTypePhotoSvc;

	/*
	 * This method will serve as addRoomType.html handler.
	 */
	@GetMapping("roomtypeinfo")
	public String getAll(ModelMap model) {
		List<RoomType> roomType = roomTypeSvc.getAll();
		model.addAttribute("roomType", roomType);
		return "front-end/roomtype/listRoomTypes";
	}
	

	
	@GetMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="房型編號: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "房型編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "房型編號: 不能小於{value}")
		@Max(value = 100, message = "房型編號: 不能超過{value}")
		@RequestParam("roomTypeId") String roomTypeId,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		RoomType roomType = roomTypeSvc.getOneRoomType(Integer.valueOf(roomTypeId));
		
		List<RoomType> list = roomTypeSvc.getAll();
		model.addAttribute("roomTypeListData", list);     // for select_page.html 第97 109行用
	
		if (roomType == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front-end/roomtype/listRoomTypes";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("roomType", roomType);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
		return "front-end/roomtype/listOneRoomType"; 
	}
}