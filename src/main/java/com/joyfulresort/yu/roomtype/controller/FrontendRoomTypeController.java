package com.joyfulresort.yu.roomtype.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
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
	@GetMapping("getAll")
	public String getAll(ModelMap model) {
		List<RoomType> roomType = roomTypeSvc.getAll();
		model.addAttribute("roomType", roomType);
		return "back-end/roomtype/listAllRoomTypes";
	}
}
//	@GetMapping("addRoomType")
//	public String addRoomType(ModelMap model) {
//		RoomType roomType = new RoomType();
//		model.addAttribute("roomType", roomType);
//		return "back-end/roomtype/addRoomType";
//	}
//
////	/*
////	 * This method will be called on addRoomType.html form submission, handling POST request It also validates the user input
////	 */
//	@PostMapping("insert")
//	public String insert(@Valid RoomType roomType, BindingResult result, ModelMap model) throws IOException {
//		System.out.println("測試點2");
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		if (result.hasErrors()) {
//			System.out.println("資料有誤");
//			return "back-end/roomtype/addRoomType";
//		}
//		/*************************** 2.開始新增資料 *****************************************/
//		roomTypeSvc.addRoomType(roomType);
//
//		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//		List<RoomType> list = roomTypeSvc.getAll();
//		model.addAttribute("roomType", list);
//		model.addAttribute("success", "- (新增成功)");
//		return "back-end/roomtype/listAllRoomTypes"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
//	}
//
//	/*
//	 * This method will be called on listAllRoomTypes.html form submission, handling
//	 * POST request
//	 */
//	@PostMapping("getOne_For_Update")
//	public String getOne_For_Update(@RequestParam("roomTypeId") String roomTypeId, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始查詢資料 *****************************************/
//		System.out.println("測試點1");
//		RoomType roomType = roomTypeSvc.getOneRoomType(Integer.valueOf(roomTypeId));
//
//		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("roomType", roomType);
//		return "back-end/roomtype/updateRoomType"; // 查詢完成後轉交updateRoomType.html
//		}
//	
//	
//	@PostMapping("update")
//	public String update(@Valid RoomType roomType, BindingResult result, ModelMap model) throws IOException {
//		System.out.println("測試點3");
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//
//		if (result.hasErrors()) {
//			System.out.println("資料不全");
//			return "back-end/roomtype/updateRoomType";
//		}
//		/*************************** 2.開始修改資料 *****************************************/
//
//		roomTypeSvc.updateRoomType(roomType);
//		System.out.println("修改成功2");
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		//model.addAttribute("success", "- (修改成功)");
//		roomType = roomTypeSvc.getOneRoomType(Integer.valueOf(roomType.getRoomTypeId()));
//		model.addAttribute("roomType", roomType);
//		return "back-end/roomtype/listAllRoomTypes"; // 修改成功後轉交listAllRoomTypes.html
//		}
//	
//
//
//}