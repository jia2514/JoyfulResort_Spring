package com.joyfulresort.so.activitysession.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.so.activity.model.ActivityService;
import com.joyfulresort.so.activity.model.ActivityVO;
import com.joyfulresort.so.activitycategory.model.ActivityCategoryVO;
import com.joyfulresort.so.activityorder.model.ActivityOrderVO;
import com.joyfulresort.so.activitysession.model.ActivitySessionService;
import com.joyfulresort.so.activitysession.model.ActivitySessionVO;
import com.redis.RedisService;

@Controller
@RequestMapping("/activitysession")
public class ActivitySessionController {
	
	@Autowired
	ActivitySessionService asSvc;
	
	@Autowired
	ActivityService aSvc;
	
	@Autowired
    private RedisService redisService;
	
	@PostMapping("listAll")
	public String getALL(ModelMap model) {
		List<ActivitySessionVO> list = asSvc.getAll();
		model.addAttribute("activitySessionListData", list);
//		model.addAttribute("getAll", true);
		return "back-end/activitysession/listAllActivitySession";
	}
	
//	@PostMapping("listOne")
//	public String getOne(@RequestParam("activitySessionID") String activitySessionID, ModelMap model) {
//		ActivitySessionVO activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
//		model.addAttribute("activitySessionVO", activitySessionVO);
//		List<ActivitySessionVO> list = asSvc.getAll();
//		model.addAttribute("activitySessionListData", list);
//		return "back-end/activitysession/listAllActivitySession";
//	}
	
	@PostMapping("listCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ActivitySessionVO> list = asSvc.getAll(map);
		model.addAttribute("activitySessionListData", list);
		
		if (list.isEmpty()) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/activitysession/activitysession";
		}
		
//		model.addAttribute("getAll", true);
		return "back-end/activitysession/listAllActivitySession";
	}
	
	@PostMapping("add")
	public String addPage(ModelMap model) {
		ActivitySessionVO activitySessionVO = new ActivitySessionVO();
		model.addAttribute("activitySessionVO", activitySessionVO);
		
		return "back-end/activitysession/addActivitySession";
	}
	
	@PostMapping("insert")
	public String insert(@Valid ActivitySessionVO activitySessionVO, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("activitySessionVO", activitySessionVO);
			return "back-end/activitysession/addActivitysession";
		}
		asSvc.addActivitySession(activitySessionVO);
		
		ActivitySessionVO activitySession = asSvc.getOneActivitySession(Integer.valueOf(activitySessionVO.getActivitySessionID()));
		model.addAttribute("activitySessionVO", activitySession);
		List<ActivitySessionVO> list = asSvc.getAll();
		model.addAttribute("activitySessionListData", list);
		
		return "back-end/activitysession/listOneActivitySession";
	}
	
	@PostMapping("updatePage")
	public String updatePage(@RequestParam("activitySessionID") String activitySessionID, ModelMap model) {
		ActivitySessionVO activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
		
		// 更新活動報名人數
		String activitySessionId = String.valueOf(activitySessionVO.getActivitySessionID());
		int enteredTotal = redisService.getEnteredTotal(activitySessionId);
		System.out.println(enteredTotal);
		activitySessionVO.setEnteredTotal(enteredTotal);
		
		model.addAttribute("activitySessionVO", activitySessionVO);
		return "back-end/activitysession/updateActivitySession";
	}
	
	@PostMapping("update")
	public String update(@Valid ActivitySessionVO activitySessionVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("activitySessionVO", activitySessionVO);
			return "back-end/activitysession/updateActivitySession";
		}
	    
	    asSvc.updateActivitySession(activitySessionVO);
	    
		activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionVO.getActivitySessionID()));
		model.addAttribute("activitySessionVO", activitySessionVO);
		return "back-end/activitysession/listOneActivitySession";
	}
	
	// 讓activitysession裡也能取得activity的資訊，可以在html中使用
	@ModelAttribute("activityListData")
	protected List<ActivityVO> referenceActivityListData() {
		List<ActivityVO> list = aSvc.getAll();
		return list;
	}
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ActivitySessionVO activitySessionVO, BindingResult result,
			String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(activitySessionVO, "activitySessionVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	// ================================================== 前台 ==================================================
	// 月歷
	@PostMapping("listSchedule")
	public void listSchedule(ModelMap model, HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		List<ActivitySessionVO> list = asSvc.getAll();
//		System.out.print(list);
		
		JSONArray jsonArray = new JSONArray(); // 創建一個JSONArray儲存JSONObject
		for(ActivitySessionVO asVO : list) {
			int asID = asVO.getActivitySessionID();
			String asName = asVO.getActivityVO().getActivityName();
			Date asDate = asVO.getActivityDate();
			int asTotal = asVO.getEnteredTotal();
			int asMax = asVO.getActivityMaxPart();
			byte asTime = asVO.getActivityTime();
			
			JSONObject obj = new JSONObject();
			obj.put("asID", asID);
			obj.put("asName", asName);
			obj.put("asDate", asDate);
			obj.put("asTotal", asTotal);
			obj.put("asMax", asMax);
			obj.put("asTime", asTime);
			
			jsonArray.put(obj); // 將JSONObject添加到JSONArray中
		}
		
		PrintWriter out = res.getWriter();
	    out.print(jsonArray.toString()); // 把JSONArray傳到前端
	    out.flush();

	}
	
	// 點選月歷中的日期
	@PostMapping("schedule")
	public void participate(@RequestParam("date") String date, ModelMap model, HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		//從前端傳來日期，以日期來搜尋該日期的活動場次
//		System.out.println(date);
		List<ActivitySessionVO> list = asSvc.getAllByDate(Date.valueOf(date));
		model.addAttribute("activitySessionList", list);
		
		JSONArray jsonArray = new JSONArray();
		for (ActivitySessionVO as : list) {
//			System.out.println(as.getActivityVO().getActivityName());
			int ID = as.getActivitySessionID();
			String Name = as.getActivityVO().getActivityName();
			
			JSONObject sch = new JSONObject();
			sch.put("ID", ID);
			sch.put("Name", Name);
			
			jsonArray.put(sch);
		}
		
		PrintWriter out = res.getWriter();
	    out.print(jsonArray.toString());
	    out.flush();
	}

}
