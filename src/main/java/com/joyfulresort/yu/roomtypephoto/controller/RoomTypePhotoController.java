package com.joyfulresort.yu.roomtypephoto.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhotoService;

@Controller
@RequestMapping("/roomtypephoto")
public class RoomTypePhotoController {
		
	@Autowired
	RoomTypePhotoService roomTypePhotoSvc;
	
	@Autowired
	RoomTypeService roomTypeSvc;
	
	@GetMapping("getAll")
	public String getAll(ModelMap model) {
		List<RoomTypePhoto> roomTypePhoto = roomTypePhotoSvc.getAll();
		model.addAttribute("roomTypePhoto", roomTypePhoto);
		return "back-end/roomtypephoto/listAllRoomTypePhotos";
	}
	
	@GetMapping("addRoomTypePhoto")
	public String addRoomTypePhoto(ModelMap model) {
		RoomTypePhoto roomTypePhoto = new RoomTypePhoto();
		model.addAttribute("roomTypePhoto", roomTypePhoto);
		return "back-end/roomtypephoto/addRoomTypePhoto";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid RoomTypePhoto roomTypePhoto, BindingResult result, ModelMap model,
			@RequestParam("roomTypePhoto") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(roomTypePhoto, result, "roomTypePhoto");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				roomTypePhoto.setRoomTypePhoto(buf);
//			}
//		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/roomtypephoto/addRoomTypePhoto";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// RoomTypePhotoService roomTypePhotoSvc = new RoomTypePhotoService();
		roomTypePhotoSvc.addRoomTypePhoto(roomTypePhoto);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<RoomTypePhoto> list = roomTypePhotoSvc.getAll();
		model.addAttribute("roomTypePhoto", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/roomtypephoto/listAllRoomTypePhotos";  //新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")

				
	}



	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("roomTyePhotoId") String roomTyePhotoId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		RoomTypePhoto roomTypePhoto = roomTypePhotoSvc.getOneRoomTypePhoto(Integer.valueOf(roomTyePhotoId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("roomTypePhoto", roomTypePhoto);
		return "back-end/roomtypephoto/updateRoomTypePhoto"; // 查詢完成後轉交updateRoomTypePhoto.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid RoomTypePhoto roomTypePhoto, BindingResult result, ModelMap model,
			@RequestParam("roomTypePhoto") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(roomTypePhoto, result, "roomTypePhoto");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] roomTypePhoto = roomTypePhotoSvc.getOneRoomTypePhoto(roomTypePhoto.length).getRoomTypePhoto();
//			roomTypePhoto.setRoomTypePhoto(roomTypePhoto);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] roomTypePhoto = multipartFile.getBytes();
//				roomTypePhoto.setRoomTypePhoto(roomTypePhoto);
//			}
//		}
		if (result.hasErrors()) {
			return "back-end/roomtype/updateRoomTypePhoto";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		roomTypePhotoSvc.updateRoomTypePhoto(roomTypePhoto);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		roomTypePhoto = roomTypePhotoSvc.getOneRoomTypePhoto(Integer.valueOf(roomTypePhoto.getRoomTypePhotoId()));
		model.addAttribute("roomTypePhoto", roomTypePhoto);
		return "back-end/roomtype/listAllRoomTypePhotos"; // 修改成功後轉交listOneRoomTypePhoto.html
	}
}
