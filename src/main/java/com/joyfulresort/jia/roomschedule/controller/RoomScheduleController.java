package com.joyfulresort.jia.roomschedule.controller;

import java.io.IOException;
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
import com.joyfulresort.jia.roomschedule.model.RoomScheduleService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

@Controller
@RequestMapping("/roomschedule")
public class RoomScheduleController {

	@Autowired
	RoomScheduleService roomScheduleSvc;

	@Autowired
	RoomTypeService roomTypeSvc;
	
	@ModelAttribute("roomTypeListData") 
	protected List<RoomType> referenceListData_RoomType(Model model) {
		model.addAttribute("roomType", new RoomType()); 
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}
	
	
	
	
	@PostMapping("getIn2Month")
	public String getAll(ModelMap model) {
		String jsonStr = roomScheduleSvc.getALLIn2Month();
		System.out.println("41+"+jsonStr);
		model.addAttribute("roomScheduleCount", jsonStr);
		return "back-end/roomschedule/listAllRoomScheduleCalendar";
	}

	@PostMapping("getCompositeQuery")
	public String listAllRoomSchedule(@ModelAttribute("roomTypeListData") List<RoomType> roomTypeListData,HttpServletRequest req, Model model) {
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
		} else if(!query.containsKey("roomTypeId")){
			String jsonStr = roomScheduleSvc.getByCompositeQuery(map);
			model.addAttribute("roomScheduleCount", jsonStr);
			return "back-end/roomschedule/listAllRoomScheduleCalendar";
			
		}else {
			String jsonStr = roomScheduleSvc.getByCompositeQuery(map);
			model.addAttribute("roomScheduleCount", jsonStr);
			return "back-end/roomschedule/listCompositeQueryRoomScheduleCalendar";
		}
		
	}

	
	/*
	 * This method will serve as addEmp.html handler.
	 */
//	@GetMapping("addOne")
//	public String addOne(ModelMap model) {
//		RoomOrder roomOrder = new RoomOrder();
//		model.addAttribute("roomOrder", roomOrder);
//		return "back-end/roomorder/addRoomOrder";
//	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST
	 * request It also validates the user input
	 */
//	@PostMapping("insert")
//	public String insert(@Valid RoomOrder roomOrder, BindingResult result, ModelMap model,
//			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(roomOrder, result, "upFiles");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				roomOrder.setUpFiles(buf);
//			}
//		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/emp/addEmp";
//		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
//		roomOrderSvc.addRoomOrder(roomOrder);
//		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//		List<RoomOrder> list = roomOrderSvc.getAll();
//		model.addAttribute("roomOrderListData", list);
//		model.addAttribute("success", "- (新增成功)");
//		return "back-end/roomorder/listAllRoomOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
//	}

//	/*
//	 * This method will be called on listAllEmp.html form submission, handling POST request
//	 */
//	@PostMapping("getOne_For_Update")
//	public String getOne_For_Update(@RequestParam("roomorderid") String roomorderid, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始查詢資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		RoomOrder roomOrder = roomOrderSvc.getOneOrder(Integer.valueOf(roomorderid));
//
//		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("roomOrder", roomOrder);
//		return "back-end/emp/updateRoomOrder"; // 查詢完成後轉交update_emp_input.html
//	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling
	 * POST request It also validates the user input
	 */
//	@PostMapping("update")
//	public String update(@Valid RoomOrder roomOrder, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(empVO, result, "upFiles");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] upFiles = empSvc.getOneEmp(empVO.getEmpno()).getUpFiles();
//			empVO.setUpFiles(upFiles);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				empVO.setUpFiles(upFiles);
//			}
//		}
//		if (result.hasErrors()) {
//			return "back-end/emp/update_emp_input";
//		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
//		roomOrderSvc.updateRoomOrder(roomOrder);
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "- (修改成功)");
//		roomOrder = roomOrderSvc.getOneRoomOrder(Integer.valueOf(roomOrder.getRoomOrderId()));
//		model.addAttribute("roomOrder", roomOrder);
//		return "back-end/roomorder/listOneRoomOrder"; // 修改成功後轉交listOneEmp.html
//	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST
	 * request
	 */
//	@PostMapping("delete")
//	public String delete(@RequestParam("empno") String empno, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始刪除資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		empSvc.deleteEmp(Integer.valueOf(empno));
//		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
//		List<EmpVO> list = empSvc.getAll();
//		model.addAttribute("empListData", list);
//		model.addAttribute("success", "- (刪除成功)");
//		return "back-end/emp/listAllEmp"; // 刪除完成後轉交listAllEmp.html
//	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : <form:select
	 * path="deptno" id="deptno" items="${deptListData}" itemValue="deptno"
	 * itemLabel="dname" />
	 */
//	@ModelAttribute("deptListData")
//	protected List<DeptVO> referenceListData() {
//		// DeptService deptSvc = new DeptService();
//		List<DeptVO> list = deptSvc.getAll();
//		return list;
//	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : <form:select
	 * path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("deptMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(RoomOrder roomOrder, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(roomOrder, "roomOrder");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}