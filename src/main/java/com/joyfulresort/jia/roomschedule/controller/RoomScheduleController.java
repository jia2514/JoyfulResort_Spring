package com.joyfulresort.jia.roomschedule.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRedisService;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

@Controller
@RequestMapping("/roomschedule")
public class RoomScheduleController {

	@Autowired
	RoomScheduleService roomScheduleSvc;
	
	@Autowired
	RoomScheduleRedisService rsRedisSvc;

	@Autowired
	RoomTypeService roomTypeSvc;

	@ModelAttribute("roomTypeListData")
	protected List<RoomType> referenceListData_RoomType(Model model) {
		model.addAttribute("roomType", new RoomType());
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}

	@PostMapping("getIn2Month")
	public String getAll(HttpServletRequest req, ModelMap model) {
		String jsonStr = roomScheduleSvc.getALLIn2Month();
		model.addAttribute("roomScheduleCount", jsonStr);
		if(req.getParameter("frontendinsert").equals("true")) {
			return "front-end/roomschedule/listAllRoomScheduleCalendar";
		}else {
			return "back-end/roomschedule/listAllRoomScheduleCalendar";
		}
		
	}

	@PostMapping("getCompositeQuery")
	public String listAllRoomSchedule(@ModelAttribute("roomTypeListData") List<RoomType> roomTypeListData,
			HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		Map<String, String> query = new HashMap<>();
		// Map.Entry即代表一組key-value

		Set<Map.Entry<String, String[]>> entry = map.entrySet();

		for (Map.Entry<String, String[]> row : entry) {
			String key = row.getKey();
			// 因為請求參數裡包含了action，做個去除動作
			if ("action".equals(key)) {
				continue;
			}
			// 若是value為空即代表沒有查詢條件，做個去除動作
			String value = row.getValue()[0]; // getValue拿到一個String陣列, 接著[0]取得第一個元素檢查
			if (value == null || value.isEmpty()) {
				continue;
			}

			query.put(key, value);
		}

		if (!query.containsKey("roomTypeId") && !query.containsKey("startquerydate")
				&& !query.containsKey("endquerydate")) {
			String jsonStr = roomScheduleSvc.getALLIn2Month();
			model.addAttribute("roomScheduleCount", jsonStr);
			return "back-end/roomschedule/listAllRoomScheduleCalendar";
		} else if (!query.containsKey("roomTypeId")) {
			String jsonStr = roomScheduleSvc.getByCompositeQuery(map);
			model.addAttribute("roomScheduleCount", jsonStr);
			return "back-end/roomschedule/listAllRoomScheduleCalendar";

		} else {
			String jsonStr = roomScheduleSvc.getByCompositeQuery(map);
			model.addAttribute("roomScheduleCount", jsonStr);
			if(req.getParameter("frontendinsert").equals("true")) {
				return "front-end/roomschedule/listOneRoomScheduleCalendar";
			}else {
				return "back-end/roomschedule/listCompositeQueryRoomScheduleCalendar";
			}
			
			
		}

	}

	@PostMapping("roomScheduleQueryList")
	public String listRoomScheduleByPeopleAmount(@ModelAttribute("roomTypeListData") List<RoomType> roomTypeListData,
			HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		Date startquerydate = Date.valueOf(req.getParameter("startquerydate"));
		Date endquerydate = Date.valueOf(req.getParameter("endquerydate"));

		long startTime = startquerydate.getTime();
		long endTime = endquerydate.getTime();
		long differenceInMilliSeconds = endTime - startTime;
		int bookingNight = (int) (differenceInMilliSeconds / (1000 * 60 * 60 * 24));

		List<Integer> totalPriceList = new ArrayList<>();
		Integer peopleAmount = Integer.valueOf(req.getParameter("peopleAmount"));
		List<Map<RoomType, Integer>> listRoomSchedule = roomScheduleSvc.getByPeopleAmount(map);
		for (Map<RoomType, Integer> roomTypeMap : listRoomSchedule) {
			int totalPrice = 0;
			for (Map.Entry<RoomType, Integer> entry : roomTypeMap.entrySet()) {
				RoomType roomType = entry.getKey();
				totalPrice += (roomType.getRoomTypePrice() * entry.getValue() * bookingNight);
			}
			totalPriceList.add(totalPrice);
		}

		model.addAttribute("listRoomSchedule", listRoomSchedule);
		model.addAttribute("startquerydate", startquerydate);
		model.addAttribute("bookingNight", bookingNight);
		model.addAttribute("endquerydate", endquerydate);
		model.addAttribute("peopleAmount", peopleAmount);
		model.addAttribute("totalPriceList", totalPriceList);
		return "front-end/roomorder/roomorderselect";

	}
	
	@PostMapping("getRoomAmountRedis")
	public ResponseEntity<Integer> getRoomAmountRedis(@RequestParam Map<String, String> formData) {
		String roomTypeId = formData.get("roomTypeId");
		String checkInDate = formData.get("checkInDate");
		String checkOutDate = formData.get("checkOutDate");

		Integer minAvailableRooms=0;
		try {
			minAvailableRooms = rsRedisSvc.getMinAvailableRooms(roomTypeId, checkInDate, checkOutDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(minAvailableRooms);
	}

}