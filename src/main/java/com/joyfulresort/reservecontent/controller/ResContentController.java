package com.joyfulresort.reservecontent.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.joyfulresort.reservecontent.model.ResContentVO;
import com.joyfulresort.reservecontent.model.ResContentService;

@Controller
@RequestMapping("/reserve")
public class ResContentController {

	@Autowired
	ResContentService rescontentSvc;

	@GetMapping("reservecontentadd")
	public String reservecontentadd(ModelMap model) {
		ResContentVO contentVO = new ResContentVO();
		model.addAttribute("contentVO", contentVO);
		return "back-end/reserve/reservecontentadd";
	}

	@PostMapping("get_for_contentupdate")
	public String get_for_contentupdate(@RequestParam("id") String id, ModelMap model) {

		ResContentVO contentVO = rescontentSvc.getOneContent(Integer.valueOf(id));
		List<ResContentVO> list = rescontentSvc.getAllContent();
		model.addAttribute("ContentList", list);
		model.addAttribute("contentVO", contentVO);

		return "back-end/reserve/reservecontentupdate";
	}

	@PostMapping("contentupdate")
	public String contentupdate(@Valid ResContentVO contentVO, BindingResult result, ModelMap model,
			@RequestParam("reserveImage") MultipartFile[] parts) throws IOException {
		result = removeFieldError(contentVO, result, "reserveImage");
		model.addAttribute("contentVO", contentVO);

		if (parts[0].isEmpty()) {
			byte[] reserveImage = rescontentSvc.getOneContent(contentVO.getId()).getReserveImage();
			contentVO.setReserveImage(reserveImage);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] reserveImage = multipartFile.getBytes();
				contentVO.setReserveImage(reserveImage);
			}

		}
		if (result.hasErrors()) {
//			  model.addAttribute("error", result.getAllErrors());
			model.addAttribute("error", result.getFieldError("reserveText"));
			return "back-end/reserve/reservecontentupdate";
		}

		rescontentSvc.updateContent(contentVO);
		List<ResContentVO> contentList = rescontentSvc.getAllContent();
		model.addAttribute("ContentList", contentList);
		contentVO = rescontentSvc.getOneContent(Integer.valueOf(contentVO.getId()));

		return "back-end/reserve/reservecontent";

	}

	@PostMapping("contentinsert")
	public String contentinsert(@Valid ResContentVO contentVO, BindingResult result, ModelMap model,
			@RequestParam("reserveImage") MultipartFile[] parts) throws IOException {
		result = removeFieldError(contentVO, result, "reserveImage");
		model.addAttribute("contentVO", contentVO);

		if (parts[0].isEmpty()) {
			model.addAttribute("message", "請上傳內容照片");

		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				contentVO.setReserveImage(buf);
			}

		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			model.addAttribute("error", result.getFieldError("reserveText"));

			return "back-end/reserve/reservecontentadd";
		}

		rescontentSvc.addContent(contentVO);
		List<ResContentVO> list = rescontentSvc.getAllContent();
		model.addAttribute("ContentList", list);

		model.addAttribute("success", "新增成功");

		return "redirect:/reserve/reservecontent";
	}

	@PostMapping("contentdelete")
	public String contentdelete(@RequestParam("id") String id, ModelMap model) {
		rescontentSvc.deleteContent(Integer.valueOf(id));
		;

		List<ResContentVO> list = rescontentSvc.getAllContent();
		model.addAttribute("ContentList", list);

		model.addAttribute("success", "刪除成功");

		return "redirect:/reserve/reservecontent";

	}

	public BindingResult removeFieldError(ResContentVO contentVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(contentVO, "contentVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;

	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : violations) {
			strBuilder.append(violation.getMessage() + "   ");

		}

		List<ResContentVO> list = rescontentSvc.getAllContent();
		model.addAttribute("ContentList", list);
//		model.addAttribute("ResList", list);// 錯誤時顯示所有清單
		String message = strBuilder.toString();
		return new ModelAndView("/back-end/reserve/reservecontentupdate", "message", message);

	}
}
