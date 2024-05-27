package com.joyfulresort.so.activitycategory.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.so.activitycategory.model.ActivityCategoryService;
import com.joyfulresort.so.activitycategory.model.ActivityCategoryVO;

@Controller
@RequestMapping("/activitycategory")
public class ActivityCategoryController {
	
	@Autowired
	ActivityCategoryService acSvc;
	
//	@PostMapping("listAll")
//	public String getALL(ModelMap model) {
//		List<ActivityCategoryVO> list = acSvc.getAll();
//		model.addAttribute("activityCategoryListData", list);
//		// 讓listAllActivityCategory可以Insert在activitycategory頁面中
//		model.addAttribute("getAll", true);
//		return "backend/activitycategory/activitycategory";
//	}
	
	@PostMapping("listOne")
	public String getOne(@RequestParam("activityCategoryID") String activityCategoryID, ModelMap model) {
		ActivityCategoryVO activityCategoryVO = acSvc.getOneActivityCategory(Integer.valueOf(activityCategoryID));
		List<ActivityCategoryVO> list = acSvc.getAll();
		model.addAttribute("activityCategoryVO", activityCategoryVO);
		model.addAttribute("activityCategoryListData", list);
//		model.addAttribute("getOne", true);
		return "back-end/activitycategory/listOneActivityCategory";
	}
	
	@PostMapping("updatePage")
	public String updatePage(@RequestParam("activityCategoryID") String activityCategoryID, ModelMap model) {
		ActivityCategoryVO activityCategoryVO = acSvc.getOneActivityCategory(Integer.valueOf(activityCategoryID));
		model.addAttribute("activityCategoryVO", activityCategoryVO);
		return "back-end/activitycategory/updateActivityCategory";
	}
	
	@PostMapping("update")
	public String update(@Valid ActivityCategoryVO activityCategoryVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("activityCategoryVO", activityCategoryVO);
			return "back-end/activitycategory/updateActivityCategory";
		}
		acSvc.updateActivityCategory(activityCategoryVO);
		activityCategoryVO = acSvc.getOneActivityCategory(Integer.valueOf(activityCategoryVO.getActivityCategoryID()));
		model.addAttribute("activityCategoryVO", activityCategoryVO);
		return "back-end/activitycategory/listOneActivityCategory";
	}
	
	@PostMapping("add")
	public String addPage(ModelMap model) {
		ActivityCategoryVO activityCategoryVO = new ActivityCategoryVO();
		model.addAttribute("activityCategoryVO", activityCategoryVO);
		return "back-end/activitycategory/addActivityCategory";
	}
	
	@PostMapping("insert")
	public String insert(@Valid ActivityCategoryVO activityCategoryVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("activityCategoryVO", activityCategoryVO);
			return "back-end/activitycategory/addActivityCategory";
		}
		acSvc.addActivityCategory(activityCategoryVO);
		List<ActivityCategoryVO> list = acSvc.getAll();
		model.addAttribute("activityCategoryListData", list);
		return "back-end/activitycategory/listOneActivityCategory";
//		model.addAttribute("activityCategoryVO", activityCategoryVO);
//		return "backend/activitycategory/listAllActivityCategory";
	}

}
