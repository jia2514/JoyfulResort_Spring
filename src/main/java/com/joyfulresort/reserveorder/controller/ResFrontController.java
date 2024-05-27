package com.joyfulresort.reserveorder.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.reservecontent.model.ResContentService;
import com.joyfulresort.reservecontent.model.ResContentVO;
import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reserveorder.model.ResVO;

@Controller
@RequestMapping("/joyfulresort")
public class ResFrontController {
	@Autowired
	MemberService memberSvc;
	@Autowired
	ResService resSvc;
	@Autowired
	ResContentService rescontentSvc;
    @Autowired
    private ResTokenService tokenService;
    
    
	@GetMapping("restaurant")
	public String restaurant(Model model) {
		return "front-end/restaurant/main";
	}

	@GetMapping("reservefrontadd") // 前端新增訂單
	public String reservefrontadd(HttpSession session, ModelMap model) {
		// 會員個人資料

		MemberVO memVO = new MemberVO();
		ResVO resVO = new ResVO();

		Object memberIDObj = session.getAttribute("memberID"); // 取得session內的值
		Integer memberID = null;
		if (memberIDObj != null) {
			memberID = Integer.parseInt(memberIDObj.toString());
		} else {
			memberIDObj = 1; //如果session為空(非登入會員)則設編號為1
			memberID = Integer.parseInt(memberIDObj.toString());
			memVO = memberSvc.getOneMember(memberID);

		}
		boolean isMember = memberID != 1;
		String memberName = "", memberPhone = "";

		if (isMember) {
			memVO = memberSvc.getOneMember(memberID); // 查找會員資料
			memberName = memVO.getMemberName();
			memberPhone = memVO.getMemberPhone();
//			model.addAttribute("memberName", memberName); // 將會員名稱加入model
//			model.addAttribute("memberName", memberPhone); // 手機
//			resVO.setResName("會員");
//			resVO.setResPhone("0000000000");
		}

		resVO.setMemberVO(memVO); // 將 SESSION 的會員資料值加進欄位值
		model.addAttribute("isMember", isMember); // 傳遞是否為會員的標誌到前端
		model.addAttribute("resVO", resVO);
		
        String token = tokenService.generateToken();
        model.addAttribute("token", token);

        
		return "front-end/restaurant/reserveorder";
	}

	@PostMapping("insertfront")
	public String insertfront(@Valid ResVO resVO, BindingResult result, HttpServletRequest request, HttpSession session,
			RedirectAttributes redirectAttributes, ModelMap model) throws IOException {
	    String token = request.getParameter("token");
        if (!tokenService.validateToken(token)) {
        	model.addAttribute("token","請勿重新下單，請重新進入訂位網頁");
    		return "front-end/restaurant/main";
        }

		MemberVO memVO = new MemberVO();

		Object memberIDObj = session.getAttribute("memberID"); // 取得session內的值
		Integer memberID = null;
		if (memberIDObj != null) {
			memberID = Integer.parseInt(memberIDObj.toString());
		} else {
			memberIDObj = 1;
			memberID = Integer.parseInt(memberIDObj.toString());
			memVO = memberSvc.getOneMember(memberID);

		}
		
		boolean isMember = memberID != 1;
		String memberName = "", memberPhone = "";

		if (isMember) {
			memVO = memberSvc.getOneMember(memberID); // 查找會員資料
			memberName = memVO.getMemberName();
			memberPhone = memVO.getMemberPhone();
			model.addAttribute("memberName", memberName); // 將會員名稱加入model
			model.addAttribute("memberName", memberPhone); // 手機
//			resVO.setResName("會員");
//			resVO.setResPhone("0000000000");
		}

		resVO.setMemberVO(memVO); // 將 SESSION 的會員資料加進來
		model.addAttribute("isMember", isMember); // 傳遞是否為會員的標誌到前端
		model.addAttribute("resVO", resVO);
		if (result.hasErrors()) {
			return "front-end/restaurant/reserveorder";
		}

		resSvc.addRes(resVO);
		redirectAttributes.addFlashAttribute("success", "新增訂單成功!");
		return "redirect:/joyfulresort/restaurant";
//		導回餐飲瀏覽
//		return "redirect:/joyfulresort/member/memberinfo";

	}
//		return "front-end/restaurant/main";
//		return "back-end/reserve/reserveorder"; 
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
