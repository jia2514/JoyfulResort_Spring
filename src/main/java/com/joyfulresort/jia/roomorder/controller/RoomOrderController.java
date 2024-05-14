package com.joyfulresort.jia.roomorder.controller;

import java.io.IOException;
import java.sql.Date;
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
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItemService;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

@Controller
@RequestMapping("/roomorder")
public class RoomOrderController {

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
	RoomOrderItem roomOrderItem;

	@Autowired
	RoomSchedule roomSchedule;

	
	@PostMapping("getByRoomOrderId")
	public ResponseEntity<Map<Integer, Object>> getByRoomOrderId(@RequestParam("roomOrderId") String roomOrderId) {
		Set<RoomOrderItem> roomOrderItems = roomOrderSvc.getRoomOrderItemsByRoomOrderId(Integer.valueOf(roomOrderId));
		Map<Integer, Object> roomOrderItemInfo = new HashMap<>();
		for(RoomOrderItem roomOrderItem : roomOrderItems) {
			Map<String, Object> item =  new HashMap<>();
			item.put("roomType", roomOrderItem.getRoomType().getRoomTypeName());
			if(roomOrderItem.getRoom()==null) {
				item.put("room", roomOrderItem.getRoom());
			}else {
				item.put("room", roomOrderItem.getRoom().getRoomId());
			}
			if(roomOrderItem.getSpecialREQ()==null) {
				item.put("specialREQ", "");
			}else {
				item.put("specialREQ", roomOrderItem.getSpecialREQ());
			}
			
			item.put("roomGuestName", roomOrderItem.getRoomGuestName());
			item.put("roomGuestPhone", roomOrderItem.getRoomGuestPhone());
			item.put("roomPrice", roomOrderItem.getRoomPrice());
			item.put("roomOrderItemState", roomOrderItem.getRoomOrderItemState());
			
			roomOrderItemInfo.put(roomOrderItem.getRoomOrderItemId(), item);
		}
		
		return ResponseEntity.ok(roomOrderItemInfo);
	}
	
	
	
	
	
	@PostMapping("getAll")
	public String getAll(ModelMap model) {
		List<RoomOrder> list = roomOrderSvc.getAll();
		model.addAttribute("roomOrderList", list);
		return "back-end/roomorder/listAllRoomOrder";
	}

	@PostMapping("list_ByCompositeQuery")
	public String listAllRoomOrder(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<RoomOrder> list = roomOrderSvc.getAll(map);
		System.out.println("65" + list);
		model.addAttribute("roomOrderList", list);
		return "back-end/roomorder/listAllRoomOrder";
	}

	@PostMapping("getOneToCancel")
	public ResponseEntity<Map<String, Object>> getOneToCancel(@RequestParam("roomOrderId") String roomOrderId) {
		Integer orderId = Integer.valueOf(roomOrderId);

		RoomOrder roomOrder = roomOrderSvc.cancelRoomOrder(orderId);

		Map<String, Object> roomOrderInfo = new HashMap<>();
		roomOrderInfo.put("roomOrderId", roomOrder.getRoomOrderId());
		roomOrderInfo.put("orderDate", roomOrder.getOrderDate());
		roomOrderInfo.put("roomOrderState", roomOrder.getRoomOrderState());
		roomOrderInfo.put("refundState", roomOrder.getRefundState());
		roomOrderInfo.put("completeDateTime", roomOrder.getCompleteDateTime());

		return ResponseEntity.ok(roomOrderInfo);
	}

	@PostMapping("getOneToRefund")
	public ResponseEntity<Map<String, Object>> getOneToRefund(@RequestParam("roomOrderId") String roomOrderId) {
		System.out.println(roomOrderId);
		Integer orderId = Integer.valueOf(roomOrderId);

		RoomOrder roomOrder = roomOrderSvc.refundRoomOrder(orderId);

		Map<String, Object> roomOrderInfo = new HashMap<>();
		roomOrderInfo.put("roomOrderId", roomOrder.getRoomOrderId());
		roomOrderInfo.put("orderDate", roomOrder.getOrderDate());
		roomOrderInfo.put("roomOrderState", roomOrder.getRoomOrderState());
		roomOrderInfo.put("refundState", roomOrder.getRefundState());
		roomOrderInfo.put("completeDateTime", roomOrder.getCompleteDateTime());

		return ResponseEntity.ok(roomOrderInfo);
	}

	@GetMapping("addOne")
	public String addOne(ModelMap model) {
		RoomOrder roomOrder = new RoomOrder();
		model.addAttribute("roomOrder", roomOrder);
		return "back-end/roomorder/addRoomOrder";
	}

	@PostMapping("insert")
	public String insert(HttpServletRequest req, Model model) throws IOException {

		Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
		req.setAttribute("errorMsgs", errorMsgs);

		/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
		String memberPhone = req.getParameter("memberPhone");


		MemberVO member = memberSvc.findByMemberPhone(memberPhone);
		Date checkInDate = java.sql.Date.valueOf(req.getParameter("checkInDate"));
		Date checkOutDate = java.sql.Date.valueOf(req.getParameter("checkOutDate"));

		roomOrder.setCheckInDate(checkInDate);
		roomOrder.setCheckOutDate(checkOutDate);
		roomOrder.setMember(member);

		long diffInMillies = checkOutDate.getTime() - checkInDate.getTime();
		int differenceInDays = (int) (diffInMillies / (1000 * 3600 * 24));
		Integer paramCount = Integer.valueOf(req.getParameter("paramCount"));
		Set<RoomOrderItem> roomOrderItems = new LinkedHashSet<>();
		Integer roomTypeId = null;
		Integer roomAmount = 0;
		
		for (int i = 1; i <= paramCount; i++) {
		    roomTypeId = Integer.valueOf(req.getParameter("roomType" + i));
		    roomAmount = Integer.valueOf(req.getParameter("roomAmount" + i));
		    if (roomAmount > 0 && roomAmount != null) {
		        for (int amount = 1; amount <= roomAmount; amount++) {
		            RoomOrderItem roomOrderItem = new RoomOrderItem(); // 在每次迭代中创建新的对象
		            roomOrderItem.setRoomOrder(roomOrder);
		            roomOrderItem.setRoomType(roomTypeSvc.getOneRoomType(roomTypeId));
		            roomOrderItem.setRoomPrice(roomTypeSvc.getOneRoomType(roomTypeId).getRoomTypePrice());
		            roomOrderItem.setSpecialREQ(req.getParameter("specialreq").trim());
		            Set<RoomSchedule> roomSchedules = new LinkedHashSet<>();
		            int count2 = 0;
		            while (count2 < differenceInDays) {
		                RoomSchedule roomSchedule = new RoomSchedule();
		                roomSchedule.setRoomType(roomTypeSvc.getOneRoomType(roomTypeId));
		                roomSchedule.setRoomOderItem(roomOrderItem);
		                long millies = checkInDate.getTime();
		                Date date = new Date(count2 * 1000 * 3600 * 24 + millies);
		                roomSchedule.setRoomScheduleDate(date);
		                roomSchedules.add(roomSchedule);
		                count2++;
		            }
		            roomOrderItem.setRoomSchedules(roomSchedules);
		            roomOrderItems.add(roomOrderItem);
		        }
		    }
		}

		roomOrder.setRoomOrderItems(roomOrderItems);

		roomOrderSvc.addRoomOrder(roomOrder);

		List<RoomOrder> listAll = roomOrderSvc.getAll();
		model.addAttribute("roomOrderList", listAll);
		return "back-end/roomorder/listAllRoomOrder";
	}


	@PostMapping("update")
	public String update(@Valid RoomOrder roomOrder, BindingResult result, ModelMap model) throws IOException {

		roomOrderSvc.updateRoomOrder(roomOrder);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		roomOrder = roomOrderSvc.getOneRoomOrder(Integer.valueOf(roomOrder.getRoomOrderId()));
		model.addAttribute("roomOrder", roomOrder);
		return "back-end/roomorder/listOneRoomOrder"; // 修改成功後轉交listOneEmp.html
	}


}