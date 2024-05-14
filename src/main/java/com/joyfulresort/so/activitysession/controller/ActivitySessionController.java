package com.joyfulresort.so.activitysession.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
		return "backend/activitysession/listAllActivitySession";
	}
	
	@PostMapping("listCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ActivitySessionVO> list = asSvc.getAll(map);
		model.addAttribute("activitySessionListData", list);
		
		if (list.isEmpty()) {
			model.addAttribute("errorMessage", "查無資料");
			return "backend/activitysession/activitysession";
		}
		
		model.addAttribute("getAll", true);
		return "backend/activitysession/listAllActivitySession";
	}
	
	@PostMapping("add")
	public String addPage(ModelMap model) {
		ActivitySessionVO activitySessionVO = new ActivitySessionVO();
		model.addAttribute("activitySessionVO", activitySessionVO);
		
		return "backend/activitysession/addActivitySession";
	}
	
	@PostMapping("insert")
	public String insert(@Valid ActivitySessionVO activitySessionVO, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("activitySessionVO", activitySessionVO);
			return "backend/activitysession/addActivitysession";
		}
		asSvc.addActivitySession(activitySessionVO);
		
		ActivitySessionVO activitySession = asSvc.getOneActivitySession(Integer.valueOf(activitySessionVO.getActivitySessionID()));
		model.addAttribute("activitySessionVO", activitySession);
		List<ActivitySessionVO> list = asSvc.getAll();
		model.addAttribute("activitySessionListData", list);
		
		return "backend/activitysession/listOneActivitySession";
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
		return "backend/activitysession/updateActivitySession";
	}
	
	@PostMapping("update")
	public String update(@Valid ActivitySessionVO activitySessionVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("activitySessionVO", activitySessionVO);
			return "backend/activitysession/updateActivitySession";
		}
	    
	    asSvc.updateActivitySession(activitySessionVO);
	    
		activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionVO.getActivitySessionID()));
		model.addAttribute("activitySessionVO", activitySessionVO);
		return "backend/activitysession/listOneActivitySession";
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

}
