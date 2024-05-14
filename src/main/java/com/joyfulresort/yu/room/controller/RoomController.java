package com.joyfulresort.yu.room.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.room.model.RoomService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomService roomSvc;

	@Autowired
	RoomTypeService roomTypeSvc;

	@GetMapping("getAll")
	public String getAll(ModelMap model) {
		List<Room> room = roomSvc.getAll();
		model.addAttribute("room", room);
		return "back-end/room/listAllRooms";
	}

	@GetMapping("addRoom")
	public String addRoom(ModelMap model) {
		Room room = new Room();
		model.addAttribute("room", room);
		return "back-end/room/addRoom";
	}

	@ModelAttribute("roomTypeListData")
	protected List<RoomType> referenceListData_RoomType(Model model) {
		model.addAttribute("roomType", new RoomType());
		System.out.println("123");
		List<RoomType> list = roomTypeSvc.getAll();
		System.out.println(list);
		return list;
	}

//	/*
//	 * This method will be called on addRoomType.html form submission, handling POST request It also validates the user input
//	 */
	@PostMapping("insert")
	public String insert(@Valid Room room, BindingResult result, HttpServletRequest req, ModelMap model)
			throws IOException {
		int roomTypeId = Integer.valueOf(req.getParameter("roomTypeId"));
		RoomType roomType = roomTypeSvc.getOneRoomType(roomTypeId);
		roomTypeSvc.getOneRoomType(roomTypeId);
		room.setRoomType(roomType);
		Boolean roomSaleState = Boolean.valueOf(req.getParameter("roomSaleState"));
		room.setRoomSaleState(roomSaleState);
		Byte roomState = Byte.parseByte(req.getParameter("roomState"));
		room.setRoomState(roomState);

		// {roomState=0, roomSaleState=1, roomTypeId=1}
		System.out.println("Q+" + roomSaleState);
		System.out.println("測試點2");
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			System.out.println("資料有誤");
			return "back-end/room/addRoom";
		}
		/*************************** 2.開始新增資料 *****************************************/
		System.out.println("測試點3" + room);
		roomSvc.addRoom(room);

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<Room> list = roomSvc.getAll();
		model.addAttribute("room", list);
		model.addAttribute("success", "- (新增成功)");
		return "back-end/room/listAllRooms"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllRooms.html form submission, handling
	 * POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("roomId") String roomId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		System.out.println("測試點1");
		Room room = roomSvc.getOneRoom(Integer.valueOf(roomId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("room", room);
		return "back-end/room/updateRoom"; // 查詢完成後轉交updateRoomType.html
	}

	@PostMapping("update")
	public String update(@Valid Room room, BindingResult result, ModelMap model) throws IOException {
		System.out.println("測試點3");

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (result.hasErrors()) {
			System.out.println("資料不全");
			return "back-end/room/updateRoom";
		}
		/*************************** 2.開始修改資料 *****************************************/

		roomSvc.updateRoom(room);
		System.out.println("修改成功2");
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		// model.addAttribute("success", "- (修改成功)");
		room = roomSvc.getOneRoom(Integer.valueOf(room.getRoomId()));
		model.addAttribute("room", room);
		return "back-end/room/listAllRooms"; // 修改成功後轉交listAllRooms.html
	}

	@ModelAttribute("roomType")
	protected List<RoomType> referenceListData() {
		// RoomTypeService roomTypeSvc = new RoomTypeService();
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}

//	--------------------------------------------------------------------------
	@PostMapping("clean")
	public ResponseEntity<Map<String, Object>> getOneToClaen(@RequestParam("roomId") String roomId) {

		Integer rId = Integer.valueOf(roomId);

		Room room = roomSvc.cleanRoom(rId);

		Map<String, Object> roomInfo = new HashMap<>();
		roomInfo.put("roomId", room.getRoomId());
		roomInfo.put("roomState", room.getRoomState());

		return ResponseEntity.ok(roomInfo);
	}

}