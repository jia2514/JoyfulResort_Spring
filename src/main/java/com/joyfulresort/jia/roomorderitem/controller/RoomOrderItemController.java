package com.joyfulresort.jia.roomorderitem.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItemService;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.room.model.RoomService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

@Controller
@RequestMapping("/roomorderitem")
public class RoomOrderItemController {

	@Autowired
	RoomOrderService roomOrderSvc;

	@Autowired
	RoomOrder roomOrder;

	@Autowired
	MemberService memberSvc;

	@Autowired
	RoomTypeService roomTypeSvc;

	@Autowired
	RoomOrderItemService roomOrderItemSvc;
	
	@Autowired
	RoomService roomSvc;

	@Autowired
	RoomOrderItem roomOrderItem;

	@Autowired
	RoomSchedule roomSchedule;

	@PostMapping("getAll")
	public String getAll(ModelMap model) {
		List<RoomOrderItem> list = roomOrderItemSvc.getAll();
		model.addAttribute("roomOrderItemList", list);
		
		return "back-end/roomorder/listAllRoomOrderItem";
	}
	
	@PostMapping("checkinUpdate")
	public ResponseEntity<Map<String, Object>> checkinUpdate(HttpServletRequest req) {
//		{specialREQ=嬰兒床及澡盆, roomGuestName=劉小雪, roomOrderItemId=12, roomGuestPhone=0967890123, roomId=4}
		String specialREQ = req.getParameter("specialREQ").trim();
		String roomGuestName = req.getParameter("roomGuestName").trim();
		String roomGuestPhone = req.getParameter("roomGuestPhone").trim();
		Integer roomOrderItemId = Integer.valueOf(req.getParameter("roomOrderItemId"));
		Integer roomId = Integer.valueOf(req.getParameter("roomId"));
		RoomOrderItem roomOrderItem = roomOrderItemSvc.getOneRoomOrderItem(roomOrderItemId);
		roomOrderItem.setSpecialREQ(specialREQ);
		roomOrderItem.setRoomGuestName(roomGuestName);
		roomOrderItem.setRoomGuestPhone(roomGuestPhone);
		roomOrderItem.setRoom(roomSvc.getOneRoom(roomId));
		roomOrderItem.setRoomOrderItemState((byte)1);
		roomSvc.getOneRoom(roomId).setRoomState((byte)1);
		roomOrderItem.getRoomOrder().setRoomOrderState((byte)4);
		RoomOrderItem orderItem = roomOrderItemSvc.updateRoomOrderItem(roomOrderItem);

		Map<String, Object> roomOrderInfo = new HashMap<>();
		roomOrderInfo.put("specialREQ", orderItem.getSpecialREQ());
		roomOrderInfo.put("roomGuestName", orderItem.getRoomGuestName());
		roomOrderInfo.put("roomGuestPhone", orderItem.getRoomGuestPhone());
		roomOrderInfo.put("roomOrderItemId", orderItem.getRoomOrderItemId());
		roomOrderInfo.put("roomId", orderItem.getRoom().getRoomId());

		return ResponseEntity.ok(roomOrderInfo);
	}
	
	@PostMapping("checkoutUpdate")
	public ResponseEntity<Map<String, Object>> getOneToCheckout(@RequestParam("roomOrderItemId") String roomOrderItemId) {
		Integer roiId = Integer.valueOf(roomOrderItemId);
		RoomOrderItem roomOrderItem = roomOrderItemSvc.checkout(roiId);
		Room room = roomOrderItem.getRoom();
		Map<String, Object> roomInfo = new HashMap<>();
		roomInfo.put("roomId", room.getRoomId());
		roomInfo.put("roomState", room.getRoomState());
		
		System.out.println("roomInfo+"+roomInfo);
		return ResponseEntity.ok(roomInfo);
	}

	
	@PostMapping("getOneToCancel")
	public ResponseEntity<Map<String, Object>> getOneToCancel(HttpServletRequest req) {
		ResponseEntity<Map<String, Object>> itemInfo = getOneToUpdate(req);
		Integer roomOrderItemId = Integer.parseInt(itemInfo.getBody().get("roomOrderItemId").toString());
		RoomOrderItem roomOrderItem = roomOrderItemSvc.cancelRoomOrderItem(roomOrderItemId);
		
		Map<String, Object> roomOrderItemInfo = new HashMap<>();
		roomOrderItemInfo.put("roomOrderItemId", roomOrderItem.getRoomOrderItemId());
		roomOrderItemInfo.put("roomOrderState", roomOrderItem.getRoomOrderItemState());
		roomOrderItemInfo.put("specialREQ", roomOrderItem.getSpecialREQ());
		return ResponseEntity.ok(roomOrderItemInfo);
	}

	@PostMapping("getOneToUpdate")
	public ResponseEntity<Map<String, Object>> getOneToUpdate(HttpServletRequest req) {
		Integer roomOrderItemId = Integer.valueOf(req.getParameter("roomOrderItemId"));
		String specialREQ = req.getParameter("specialREQ");
		RoomOrderItem roomOrderItem = roomOrderItemSvc.getOneRoomOrderItem(roomOrderItemId);
		roomOrderItem.setSpecialREQ(specialREQ);
		RoomOrderItem roomOrderItem2 = roomOrderItemSvc.updateRoomOrderItem(roomOrderItem);

		Map<String, Object> roomOrderItemInfo = new HashMap<>();
		roomOrderItemInfo.put("roomOrderItemId", roomOrderItem2.getRoomOrderItemId());
		roomOrderItemInfo.put("specialREQ", roomOrderItem2.getSpecialREQ());
		
		return ResponseEntity.ok(roomOrderItemInfo);
	}

	
	
	
	
	
//	@PostMapping("list_ByCompositeQuery")
//	public String listAllRoomOrder(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<RoomOrder> list = roomOrderSvc.getAll(map);
//		System.out.println("65" + list);
//		model.addAttribute("roomOrderList", list);
//		return "back-end/roomorder/listAllRoomOrder";
//	}
	
	
}