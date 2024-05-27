package com.joyfulresort.so.activityorder.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.so.activityorder.model.ActivityOrderService;
import com.joyfulresort.so.activityorder.model.ActivityOrderVO;
import com.joyfulresort.so.activitysession.model.ActivitySessionService;
import com.joyfulresort.so.activitysession.model.ActivitySessionVO;
import com.redis.RedisService;

@Controller
@RequestMapping("/activityorder")
public class ActivityOrderController {
	
	@Autowired
	ActivityOrderService aoSvc;
	
	@Autowired
	ActivitySessionService asSvc;
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
	MemberService memSvc;
	
	@PostMapping("listAll")
	public String getALL(ModelMap model) {
		List<ActivityOrderVO> list = aoSvc.getAll();
		model.addAttribute("activityOrderListData", list);
		model.addAttribute("getAll", true);
		return "back-end/activityorder/listAllActivityOrder";
	}
	
	@PostMapping("listType")
	public String getALLByType(@RequestParam("activitySessionID") String activitySessionID, ModelMap model) {
		List<ActivityOrderVO> list = aoSvc.getActivityBySession(Integer.valueOf(activitySessionID));
		model.addAttribute("activityOrderListData", list);
		
		if (list.isEmpty()) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/activityorder/activityorder";
		}
		
		return "back-end/activityorder/listAllActivityOrder";
	}
	
	@PostMapping("listCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ActivityOrderVO> list = aoSvc.getAll(map);
		model.addAttribute("activityOrderListData", list);
		
		if (list.isEmpty()) {
			model.addAttribute("errorMessage", "查無資料");
			model.addAttribute("activityOrderListData", list);
			return "back-end/activityorder/activityorder";
		}
		
		model.addAttribute("getAll", true);
		return "back-end/activityorder/listAllActivityOrder";
	}
	
	@PostMapping("updatePage")
	public String updatePage(@RequestParam("activityOrderID") String activityOrderID, ModelMap model) {
		ActivityOrderVO activityOrderVO = aoSvc.getOneActivityOrder(Integer.valueOf(activityOrderID));
		model.addAttribute("activityOrderVO", activityOrderVO);
//		System.out.println(activityOrderVO.getActivitySessionVO().getActivitySessionID());
//		ActivitySessionVO activitySessionVO = activityOrderVO.getActivitySessionVO();
//		model.addAttribute("activitySessionVO", activitySessionVO);
		
		
		return "back-end/activityorder/updateActivityOrder";
	}
	
	@PostMapping("update")
	public String update(@Valid ActivityOrderVO activityOrderVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("activityOrderVO", activityOrderVO);
			return "back-end/activityorder/updateActivityOrder";
		}
		aoSvc.updateActivityOrder(activityOrderVO);
		
		// 如果訂單狀態為3(取消)，即已完成，則從 Redis 中減少報名人數
		String activitySessionId = String.valueOf(activityOrderVO.getActivitySessionVO().getActivitySessionID());
        if (activityOrderVO.getOrderStatus() == 3) {
            redisService.updateActivityOrder(activitySessionId, activityOrderVO.getEnteredNumber());
        }
		
		activityOrderVO = aoSvc.getOneActivityOrder(Integer.valueOf(activityOrderVO.getActivityOrderID()));
		model.addAttribute("activityOrderVO", activityOrderVO);
		return "back-end/activityorder/listOneActivityOrder";
	}
	
	@PostMapping("add")
	public String addPage(ModelMap model) {
//		ActivitySessionVO activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
//		model.addAttribute("activitySessionVO", activitySessionVO);
//		System.out.println(activitySessionVO.getActivitySessionID());
		
		// 取得當前時間戳記
	    long currentTimeMillis = System.currentTimeMillis();
	    Timestamp orderTime = new Timestamp(currentTimeMillis);
	    // 將時間戳記設置到模型中
	    model.addAttribute("orderTime", orderTime);
		
	    ActivityOrderVO activityOrderVO = new ActivityOrderVO();	
		model.addAttribute("activityOrderVO", activityOrderVO);	
		return "back-end/activityorder/addActivityOrder";
	}
	
	@PostMapping("insert")
	public String insert(@Valid ActivityOrderVO activityOrderVO, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("activityOrderVO", activityOrderVO);
			return "back-end/activityorder/addActivityOrder";
		}
		
		// 獲取當前活動場次的報名人數
	    String activitySessionId = String.valueOf(activityOrderVO.getActivitySessionVO().getActivitySessionID());
//	    System.out.println(activitySessionId);
	    int currentTotal = redisService.getEnteredTotal(activitySessionId);
//	    System.out.println(currentTotal);
	    int enteredNumber = activityOrderVO.getEnteredNumber();
	    // 獲取當前活動場次的報名人數上限
	    ActivitySessionVO asVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionId));
	    int maxTotal = asVO.getActivityMaxPart();

	    // 檢查報名人數是否已滿
	    if (currentTotal + enteredNumber > maxTotal) {
	        // 已滿，不能新增
	        // 返回錯誤信息或重定向到適當的頁面
	    	int leftTotal = maxTotal - currentTotal;
	        model.addAttribute("errorMessage", "報名人數剩 " + leftTotal + " 人");
	        return "back-end/activityorder/addActivityOrder"; // 需要創建一個錯誤頁面或適當處理
	    }
		
		aoSvc.addActivityOrder(activityOrderVO);
		
		// 更新 Redis 中的報名人數
        String activitySession = String.valueOf(activityOrderVO.getActivitySessionVO().getActivitySessionID());
        redisService.addActivityOrder(activitySession, activityOrderVO.getEnteredNumber());
		
		ActivityOrderVO activityOrder = aoSvc.getOneActivityOrder(Integer.valueOf(activityOrderVO.getActivityOrderID()));
		model.addAttribute("activityOrderVO", activityOrder);
		List<ActivityOrderVO> list = aoSvc.getAll();
		model.addAttribute("activityOrderListData", list);
		
		return "back-end/activityorder/listOneActivityOrder";
	}
	
	// ======================================== 接收前台ajax請求 ========================================
	
	@PostMapping("getActivityPrice")
	public void getActivityPrice(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		
		String activitySessionID = req.getParameter("activitySessionID");
//		System.out.println(activitySessionID);
		
		String enteredNumber = req.getParameter("enteredNumber");
//		System.out.println(enteredNumber);
		
		ActivitySessionVO asVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
		Integer acPrice = asVO.getActivityVO().getActivityPrice();
		Integer activityPrice = acPrice * (Integer.valueOf(enteredNumber));
//		System.out.println(activityPrice);
		// 把金額回傳到前端
		JSONObject obj = new JSONObject();
		obj.put("activityPrice", activityPrice);
		res.getWriter().print(obj);
	}
	
	@PostMapping("getLeftTotal")
	public void getLeftTotal(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json; charset=UTF-8");
		
		String activitySessionID = req.getParameter("activitySessionID");
//		System.out.println(activitySessionID);
		
		// 獲取當前活動場次的剩餘報名人數
	    int currentTotal = redisService.getEnteredTotal(activitySessionID);
	    // 獲取當前活動場次的報名人數上限
	    ActivitySessionVO asVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
	    int maxTotal = asVO.getActivityMaxPart();
	    
	    int leftTotal = maxTotal - currentTotal;
//	    System.out.println(leftTotal);
	    // 把剩餘報名人數回傳到前端
	    JSONObject obj = new JSONObject();
		obj.put("leftTotal", leftTotal);
		res.getWriter().print(obj);
		
	}
	
	// 讓activity裡也能取得activitysession的資訊，可以在html中使用
	@ModelAttribute("activitySessionListData")
	protected List<ActivitySessionVO> referenceActivitySessionListData() {
		List<ActivitySessionVO> list = asSvc.getAll();
		return list;
	}
	
	@ModelAttribute("MemberList")
	protected List<MemberVO> referenceMemberList(Model model) {
		List<MemberVO> list = memSvc.getAll();
		return list;
	}
	
	// ================================================== 前台 ==================================================
	
		@PostMapping("addOrder")
		public String addOrder(ModelMap model, ServletRequest request) {
//			ActivitySessionVO activitySessionVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionID));
//			model.addAttribute("activitySessionVO", activitySessionVO);
//			System.out.println(activitySessionVO.getActivitySessionID());
			
			// 取得當前時間戳記
		    long currentTimeMillis = System.currentTimeMillis();
		    Timestamp orderTime = new Timestamp(currentTimeMillis);
		    // 將時間戳記設置到模型中
		    model.addAttribute("orderTime", orderTime);
		    
		    // 取得登入後的memberID
		    HttpServletRequest req = (HttpServletRequest) request;
		    HttpSession session = req.getSession();
		    String memberId = String.valueOf(session.getAttribute("memberID"));
		    MemberVO member = memSvc.getOneMember(Integer.valueOf(memberId));
		    model.addAttribute("memberVO", member);
			
		    ActivityOrderVO activityOrderVO = new ActivityOrderVO();	
		    activityOrderVO.setMemberVO(member);
			model.addAttribute("activityOrderVO", activityOrderVO);	
			return "front-end/activity/participate";
		}
		
		@PostMapping("insertOrder")
		public String insertOrder(@Valid ActivityOrderVO activityOrderVO, BindingResult result, ModelMap model, ServletRequest request) {

			// 獲取當前活動場次的報名人數
		    String activitySessionId = String.valueOf(activityOrderVO.getActivitySessionVO().getActivitySessionID());
//		    System.out.println(activitySessionId);
		    int currentTotal = redisService.getEnteredTotal(activitySessionId);
//		    System.out.println(currentTotal);
		    int enteredNumber = activityOrderVO.getEnteredNumber();
		    // 獲取當前活動場次的報名人數上限
		    ActivitySessionVO asVO = asSvc.getOneActivitySession(Integer.valueOf(activitySessionId));
		    int maxTotal = asVO.getActivityMaxPart();

		    // 檢查報名人數是否已滿
		    if (currentTotal + enteredNumber > maxTotal) {
		        // 已滿，不能新增
		        // 返回錯誤信息或重定向到適當的頁面
		    	int leftTotal = maxTotal - currentTotal;
		        model.addAttribute("errorMessage", "報名人數剩 " + leftTotal + " 人");
		        
		        // 重定向取得登入後的會員資料
		        HttpServletRequest req = (HttpServletRequest) request;
			    HttpSession session = req.getSession();
			    String memberId = String.valueOf(session.getAttribute("memberID"));
			    MemberVO member = memSvc.getOneMember(Integer.valueOf(memberId));
			    model.addAttribute("memberVO", member);
			    activityOrderVO.setMemberVO(member);
		        
		        model.addAttribute("activityOrderVO", activityOrderVO);
		        return "front-end/activity/participate"; // 需要創建一個錯誤頁面或適當處理
		    }
			
			aoSvc.addActivityOrder(activityOrderVO);
			
			// 更新 Redis 中的報名人數
	        String activitySession = String.valueOf(activityOrderVO.getActivitySessionVO().getActivitySessionID());
	        redisService.addActivityOrder(activitySession, activityOrderVO.getEnteredNumber());
	        
			// ==================== 下列資料讓orderdetails獲取需要的資料 ====================
	        // 取得登入後的會員資料
		    HttpServletRequest req = (HttpServletRequest) request;
		    HttpSession session = req.getSession();
		    String memberId = String.valueOf(session.getAttribute("memberID"));
		    MemberVO member = memSvc.getOneMember(Integer.valueOf(memberId));	
			model.addAttribute("memberVO", member);
			
			// 取得活動名稱
			ActivitySessionVO as = asSvc.getOneActivitySession(activityOrderVO.getActivitySessionVO().getActivitySessionID());
			model.addAttribute("asVO", as);
			
			ActivityOrderVO activityOrder = aoSvc.getOneActivityOrder(Integer.valueOf(activityOrderVO.getActivityOrderID()));
			model.addAttribute("activityOrderVO", activityOrder);
			
			return "front-end/activity/orderdetails";
		}

}
