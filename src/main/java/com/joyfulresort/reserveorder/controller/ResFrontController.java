package com.joyfulresort.reserveorder.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.joyfulresort.reservecontent.model.ResContentVO;
import com.joyfulresort.reservecontent.model.ResContentService;
import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reserveorder.model.ResVO;

@Controller
@RequestMapping("/joyfulresort")
public class ResFrontController {

	@Autowired
	ResService resSvc;
	@Autowired
	ResContentService rescontentSvc;

	@GetMapping("restaurant")
	public String restaurant(Model model) {
		return "front-end/restaurant/main";
	}

	@GetMapping("reservefrontadd") // 前端新增訂單
	public String reservefrontadd(ModelMap model) {
		ResVO resVO = new ResVO();
		model.addAttribute("resVO", resVO);

		return "front-end/restaurant/reserveorder";
	}

	@PostMapping("insertfront")
	public String insertfront(@Valid ResVO resVO, BindingResult result, HttpServletRequest request, ModelMap model)
			throws IOException {
		if (result.hasErrors()) {
//			 model.addAttribute("reserveNumberError", result.getFieldError("reserveNumber").getDefaultMessage());
//		     model.addAttribute("bookingDateError", result.getFieldError("bookingDate").getDefaultMessage());
//		     model.addAttribute("error", result.getFieldError().getDefaultMessage());
			return "front-end/restaurant/reserveorder";
		}
		resSvc.addRes(resVO);
		model.addAttribute("success", "訂位成功");
		return "front-end/restaurant/main";
	}
//		return "redirect:/joyfulresort/restaurant"  
	// 也沒問題
//		return "back-end/reserve/reserveorder"; 
	// 沒問題
//		會多報Request method 'GET' not supported]   
//		在Index控制層76行多設置一個getmapping才不會抱錯

	@ModelAttribute("ContentList")
	protected List<ResContentVO> referenceContentList(Model model) {

		List<ResContentVO> list = rescontentSvc.getAllContent();
		return list;
	}
	
	
	
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			strBuilder.append(violation.getMessage() + "<br>");

		}

//		List<ResVO> list = resSvc.getAllRes();
//		model.addAttribute("ResListData", list);
//		model.addAttribute("ResList", list);// 錯誤時顯示所有清單
		String message = strBuilder.toString();
		return new ModelAndView("front-end/restaurant/reserveorder", "message", message);

	}
}
