package com.joyfulresort.jia.roomorder.controller;

import java.io.Console;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItemService;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRedisService;
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
	
	@Autowired
	RoomScheduleRedisService rsRedisSvc;
	
	@ModelAttribute("roomTypeListData")
	protected List<RoomType> referenceListData_RoomType(Model model) {
		model.addAttribute("roomType", new RoomType());
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}
	
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
		model.addAttribute("roomOrderList", list);
		return "back-end/roomorder/listAllRoomOrder";
	}

	@PostMapping("getOneToCancel")
	public ResponseEntity<Map<String, Object>> getOneToCancel(@RequestParam("roomOrderId") String roomOrderId) {
		Integer orderId = Integer.valueOf(roomOrderId);

		RoomOrder roomOrder = roomOrderSvc.cancelRoomOrder(orderId,(byte)4);

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


	
	@PostMapping("frontendAddOne")
	public String frontendAddOne(@RequestParam Map<String, String> formData, Model model) {
	    model.addAttribute("roomOrder", formData);
	    List<RoomType> list = roomTypeSvc.getAll();
	    model.addAttribute("roomTypeListData", list);

	    return "front-end/roomorder/addRoomOrder";
	}
	
	
//  為了解決在重新整理頁面時重複執行控制器操作並導致重複下單的問題，採用 POST-REDIRECT-GET (PRG) 設計模式。防止當用戶重新整理頁面時重複提交表單。
	@PostMapping("insert")
	public String insert(HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) throws IOException {
	    String memberPhone = req.getParameter("memberPhone");

	    MemberVO member = memberSvc.findByMemberPhone(memberPhone);
	    Date checkInDate = java.sql.Date.valueOf(req.getParameter("checkInDate"));
	    Date checkOutDate = java.sql.Date.valueOf(req.getParameter("checkOutDate"));

	    RoomOrder roomOrder = new RoomOrder();
	    roomOrder.setCheckInDate(checkInDate);
	    roomOrder.setCheckOutDate(checkOutDate);
	    roomOrder.setMember(member);
	    System.out.println(member.getMemberState());
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
	                RoomOrderItem roomOrderItem = new RoomOrderItem(); 
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
	                    
	                    rsRedisSvc.addOrDeleteRedisRoomSchedule(date, roomTypeId, 1, -1);
	                  
	                }
	                roomOrderItem.setRoomSchedules(roomSchedules);
	                roomOrderItems.add(roomOrderItem);
	            }
	        }
	    }

	    roomOrder.setRoomOrderItems(roomOrderItems);

	    RoomOrder newRoomOrder = roomOrderSvc.addRoomOrder(roomOrder);

	    String totalPrice = req.getParameter("totalPrice");
	    String redirectUrl;
	    if (req.getParameter("frontendinsert").equals("true")) {
	        redirectUrl = "redirect:/front-end/roomorder/listOneRoomOrder?roomOrderId=" + newRoomOrder.getRoomOrderId() + "&totalPrice=" + totalPrice;
	    } else {
	        redirectUrl = "redirect:/back-end/roomorder/listOneRoomOrder?roomOrderId=" + newRoomOrder.getRoomOrderId() + "&totalPrice=" + totalPrice;
	    }
	    return redirectUrl;
	}

	
	
	
	
	
	@PostMapping("checkMember")
	public ResponseEntity<Map<String, Object>> checkMember(@RequestParam("memberPhone") String memberPhone) {
		MemberVO member = memberSvc.findByMemberPhone(memberPhone);
		Map<String, Object> memberInfo = new HashMap<>();
		if(member == null) {
			memberInfo.put("memberName", "null");
		}else {
			memberInfo.put("memberName", member.getMemberName());
			memberInfo.put("memberId", member.getMemberId());
		}

		return ResponseEntity.ok(memberInfo);
	}

	@PostMapping("getRoomTypeByRoomTypeId")
	public ResponseEntity<RoomType> getRoomTypeByRoomTypeId(@RequestParam("roomTypeId") Integer roomTypeId) {
		RoomType roomType =roomTypeSvc.getOnebyId(roomTypeId);
		return ResponseEntity.ok(roomType);
	}

	@PostMapping("memberAjax")
	public ResponseEntity<MemberVO> memberAjax(@RequestParam("memberId") Integer memberId) {
		
		MemberVO member = memberSvc.getOneMember(memberId);
		return ResponseEntity.ok(member);
	}
	
	

}