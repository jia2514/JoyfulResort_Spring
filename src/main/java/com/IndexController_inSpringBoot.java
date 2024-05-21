package com;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.fun.authorityfunction.model.AuthorityFunction;
import com.joyfulresort.fun.authorityfunction.model.AuthorityFunctionService;
import com.joyfulresort.fun.emp.model.Employee;
import com.joyfulresort.fun.emp.model.EmployeeService;
import com.joyfulresort.fun.position.model.Position;
import com.joyfulresort.fun.position.model.PositionService;
import com.joyfulresort.fun.positionauthority.model.PositionAuthority;
import com.joyfulresort.fun.positionauthority.model.PositionAuthorityService;
import com.joyfulresort.he.member.model.MemberService;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItemService;
import com.joyfulresort.yu.newslist.model.NewsList;
import com.joyfulresort.yu.newslist.model.NewsListService;
import com.joyfulresort.reservecontent.model.ResContentService;
import com.joyfulresort.reservecontent.model.ResContentVO;
import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reserveorder.model.ResVO;
import com.joyfulresort.reservesession.model.RessionService;
import com.joyfulresort.reservesession.model.RessionVO;
import com.joyfulresort.so.activity.model.ActivityService;
import com.joyfulresort.so.activity.model.ActivityVO;
import com.joyfulresort.so.activitycategory.model.ActivityCategoryService;
import com.joyfulresort.so.activitycategory.model.ActivityCategoryVO;
import com.joyfulresort.so.activityorder.model.ActivityOrderService;
import com.joyfulresort.so.activityorder.model.ActivityOrderVO;
import com.joyfulresort.so.activityphoto.model.ActivityPhotoService;
import com.joyfulresort.so.activityphoto.model.ActivityPhotoVO;
import com.joyfulresort.so.activitysession.model.ActivitySessionService;
import com.joyfulresort.so.activitysession.model.ActivitySessionVO;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.room.model.RoomService;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot {

	/*
	 * +-----------------------------------------------------------+
	 * |.........................共同/首頁...........................|
	 * +-----------------------------------------------------------+
	 */

	@Value("${welcome.message}")
	private String message;

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("message", message);
		return "index"; // view
	}

	//
	@GetMapping("/backend_main_page")
	public String backend_main_page(Model model) {
		return "back-end/main_page";
	}

	@GetMapping("/joyfulresort")
	public String frontend_main_page(Model model) {
		return "front-end/main_page";
	}
	
	@GetMapping("/backend_login")
	public String backend_login(Model model) {
		return "back-end/backend_login";
	}

	/*
	 * +-----------------------------------------------------------+
	 * |.........................會員相關............................|
	 * +-----------------------------------------------------------+
	 */
	@Autowired
	MemberService memSvc;

	@GetMapping("/member/listAllMember")
	public String listAllMember(Model model) {
		List<MemberVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);
		return "back-end/member/listAllMember";
	}

	
	

	@ModelAttribute("MemberList")
	protected List<MemberVO> referenceMemberList(Model model) {

		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	
	
	/*
	 * +-----------------------------------------------------------+
	 * |.........................訂房相關............................|
	 * +-----------------------------------------------------------+
	 */
	@Autowired
	RoomOrderService roomOrderSvc;

	@Autowired
	RoomTypeService roomTypeSvc;

	@Autowired
	RoomOrderItemService roomOrderItemSvc;

	@Autowired
	RoomService roomSvc;

	@ModelAttribute("roomTypeListData")
	protected List<RoomType> referenceListData_RoomType(Model model) {
		model.addAttribute("roomType", new RoomType());
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}

	@ModelAttribute("roomListData")
	protected List<Room> referenceListData_Room(Model model) {
		model.addAttribute("room", new Room());
		List<Room> list = roomSvc.getAll();
		return list;
	}

	@ModelAttribute("roomType") // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<RoomType> referenceroomTypeListData(Model model) {
		List<RoomType> list = roomTypeSvc.getAll();
		return list;
	}

	@ModelAttribute("room") // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<Room> referenceroomListData(Model model) {
		List<Room> list = roomSvc.getAll();
		return list;
	}

//-------------------------roomorder-------------------------

	@ModelAttribute("roomOrderListData")
	protected List<RoomOrder> referenceListData_RoomOrder(Model model) {
		List<RoomOrder> list = roomOrderSvc.getAll();
		return list;
	}

	@GetMapping("/roomorder/roomorderselect")
	public String roomorderselect(Model model) {
		return "back-end/roomorder/roomorderselect";
	}

	@GetMapping("/roomorder/listAllRoomOrder")
	public String listAllRoomOrder(Model model) {
		return "back-end/roomorder/listAllRoomOrder";
	}

	@GetMapping("/roomorder/addRoomOrder")
	public String addRoomOrder(@RequestParam Map<String, String> rq, Model model) {
		System.out.println(rq);
		model.addAttribute("roomOrder", rq);
		return "back-end/roomorder/addRoomOrder";
	}

//-------------------------roomschedule-------------------------

	@GetMapping("/roomschedule/roomscheduleselect")
	public String roomscheduleselect(Model model) {
		return "back-end/roomschedule/roomscheduleselect";
	}

//-------------------------roomorderitem-------------------------    

	@GetMapping("/roomorderitem/checkin")
	public String listCheckInItem(Model model) {

		List<RoomOrder> list = roomOrderSvc.getTodayCheckIn();
		System.out.println(list);
		model.addAttribute("checkInList", list);
		return "back-end/roomorderitem/checkin";
	}

	@GetMapping("/roomorderitem/checkout")
	public String listCheckOutItem(Model model) {
		List<RoomOrder> list = roomOrderSvc.getTodayCheckOut();
		System.out.println(list);
		model.addAttribute("checkOutList", list);
		return "back-end/roomorderitem/checkout";
	}

//-------------------------roomtype-------------------------    
	@GetMapping("/roomtype/roomtypeselect")
	public String roomTypeSelect(Model model) {
		return "back-end/roomtype/roomtypeselect";
	}

//  @GetMapping("/roomtype/addRoomType")
// 	public String addRoomType(Model model) {
// 		return "back-end/roomtype/addRoomType";
// 	}

	@GetMapping("/roomtype/listAllRoomTypes")
	public String listAllRoomTypes(Model model) {
		return "back-end/roomtype/listAllRoomTypes";
	}

	@GetMapping("/roomtype/listCompositeQueryRoomTypes")
	public String listCompositeQueryRoomTypes(Model model) {
		return "back-end/roomtype/listCompositeQueryRoomTypes";
	}

	@GetMapping("/roomtype/listOneRoomType")
	public String listOneRoomType(Model model) {
		return "back-end/roomtype/listOneRoomType";
	}

	@GetMapping("/roomtype/updateRoomType")
	public String updateRoomType(Model model) {
		return "back-end/roomtype/updateRoomType";
	}

//-------------------------room-------------------------    
	@GetMapping("/room/roomselect")
	public String roomSelect(Model model) {
		return "back-end/room/roomselect";
	}

//     @GetMapping("/room/addRoom")
//    	public String addRoom(Model model) {
//    		return "back-end/room/addRoom";
//    	}

	@GetMapping("/room/listAllRooms")
	public String listAllRooms(Model model) {
		return "back-end/room/listAllRooms";
	}

	@GetMapping("/room/listCompositeQueryRooms")
	public String listCompositeQueryRooms(Model model) {
		return "back-end/room/listCompositeQueryRooms";
	}

	@GetMapping("/room/listOneRoom")
	public String listOneRoom(Model model) {
		return "back-end/room/listOneRoom";
	}

	@GetMapping("/room/updateRoom")
	public String updateRoomT(Model model) {
		return "back-end/room/updateRoom";
	}

//-------------------------roomphoto-------------------------    
	@GetMapping("/roomtypephoto/roomtypephotoselect")
	public String roomTypePhotoSelect(Model model) {
		return "back-end/roomtypephoto/roomtypephotoselect";
	}

// @GetMapping("/roomtypephoto/addRoomTypePhoto")
//	public String addRoomTypePhoto(Model model) {
//		return "back-end/roomtypephoto/addRoomTypePhoto";
//	}

	@GetMapping("/roomtypephoto/listAllRoomPhotos")
	public String listAllRoomPhotos(Model model) {
		return "back-end/roomtypephoto/listAllRoomPhotos";
	}

	@GetMapping("/roomtypephoto/updateRoomTypePhoto")
	public String updateRoomTypePhoto(Model model) {
		return "back-end/roomtypephoto/updateRoomTypePhoto";
	}

	/*
	 * +-----------------------------------------------------------+
	 * |.........................活動相關............................|
	 * +-----------------------------------------------------------+
	 */
	@Autowired
	ActivityCategoryService acSvc;

	@Autowired
	ActivityService aSvc;

	@Autowired
	ActivityPhotoService apSvc;

	@Autowired
	ActivitySessionService asSvc;

	@Autowired
	ActivityOrderService aoSvc;
	
	// ======================================== 前台頁面 ======================================== //
	
	@GetMapping("/activityinfo")
	public String activityInfo(Model model) {
		return "front-end/activity/activityinfo";
	}
	
	@GetMapping("/joyfulactivity")
	public String joyfulActivity(Model model) {
		return "front-end/activity/joyfulactivity";
	}
	
	@GetMapping("/participate")
	public String participate(Model model) {
		return "front-end/activity/participate";
	}
	
	// ======================================== 後台控制器 ======================================== //
	
	@GetMapping("/activitycategory/activitycategory")
	public String activityCategory(Model model) {
		return "back-end/activitycategory/activitycategory";
	}

	@GetMapping("/activity/activity")
	public String activity(Model model) {
		return "back-end/activity/activity";
	}

	@GetMapping("/activityphoto/activityphoto")
	public String activityPhoto(Model model) {
		return "back-end/activityphoto/activityphoto";
	}

	@GetMapping("/activitysession/activitysession")
	public String activitySession(Model model) {
		return "back-end/activitysession/activitysession";
	}

	@GetMapping("/activityorder/activityorder")
	public String activityOrder(Model model) {
		return "back-end/activityorder/activityorder";
	}

	// ============================== 讓GetMapping的頁面載入資料 ============================== //

	@ModelAttribute("activityCategoryListData")
	protected List<ActivityCategoryVO> referenceActivityCategoryListData(Model model) {
		List<ActivityCategoryVO> activityCategoryListData = acSvc.getAll();
		return activityCategoryListData;
	}

	@ModelAttribute("activityListData")
	protected List<ActivityVO> referenceActivityListData(Model model) {
		List<ActivityVO> activityListData = aSvc.getAll();
		return activityListData;
	}

	@ModelAttribute("activityPhotoListData")
	protected List<ActivityPhotoVO> referenceActivityPhotoListData(Model model) {
		List<ActivityPhotoVO> activityPhotoVOListData = apSvc.getAll();
		return activityPhotoVOListData;
	}

	@ModelAttribute("activitySessionListData")
	protected List<ActivitySessionVO> referenceActivitySessionListData(Model model) {
		List<ActivitySessionVO> activitySessionListData = asSvc.getAll();
		return activitySessionListData;
	}

	@ModelAttribute("activityOrderListData")
	protected List<ActivityOrderVO> referenceActivityOrderListData(Model model) {
		List<ActivityOrderVO> activityOrderListData = aoSvc.getAll();
		return activityOrderListData;
	}

	/*
	 * +-----------------------------------------------------------+
	 * |.........................會議廳相關..........................|
	 * +-----------------------------------------------------------+
	 */

	
	
	/*
	 * +-----------------------------------------------------------+
	 * |.........................餐廳相關............................|
	 * +-----------------------------------------------------------+
	 */
	@Autowired
	MemberService memberSvc;
	@Autowired
	RessionService ressionSvc;
	@Autowired
	ResService resSvc;
	@Autowired
	ResContentService rescontentSvc;
	
	@GetMapping("/reserve/reserveorder")
	public String listAllres(Model model) {
		return "back-end/reserve/reserveorder";
	}

	@GetMapping("/reserve/reservecontent")
	public String reservecontent(Model model) {
		return "back-end/reserve/reservecontent";
	}
	
	@GetMapping("/joyfulresort/insertfront")  //配合前端新增訂單不報錯控制層而設置
	public String restaurant1(Model model) {
		return "front-end/restaurant/main";
	}

	
	
//	----------------------------------------------
	
	@ModelAttribute("ContentList")
	protected List<ResContentVO> referenceContentList(Model model) {

		List<ResContentVO> list = rescontentSvc.getAllContent();
		return list;
	}
	
	@ModelAttribute("ResList")
	protected List<ResVO> referenceResList(Model model) {
		List<ResVO> list = resSvc.getAllRes();
		return list;
	}

	@ModelAttribute("ResListData")
	protected List<ResVO> referenceResListData(Model model) {

		List<ResVO> list = resSvc.getAllRes();// 首次進入 讓下拉式選單抓資料
		return list;
	}

	@ModelAttribute("ResssionList")
	protected List<RessionVO> referenceRessionList(Model model) {

		List<RessionVO> list = ressionSvc.getAllRessions();
		return list;
	}
	

	
	/*
	 * +-----------------------------------------------------------+
	 * |.........................員工相關............................|
	 * +-----------------------------------------------------------+
	 */
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	AuthorityFunctionService authorityFunctionService;
	
	@Autowired
	PositionAuthorityService positionAuthorityService;
	
	@PostMapping("/employee/updateEmployee")
    public String updateEmployee(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點indexupdateEmployee");
        return "back-end/employee/updateEmployee"; //view
    }
    
    
    @GetMapping("/employee/listAllEmployee")
    public String listAllEmployee(@RequestParam(defaultValue = "0") int page,Model model) {
        // 在這裡執行你的分頁邏輯
       
        int size = 10; // 預設每頁大小
//        這邊可以理解為查詢第幾頁，而一頁有幾筆資料
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.findAll(pageable);
        
//        System.out.println("總共頁數：" + employeePage.getTotalPages());
//        System.out.println("總共記錄數：" + employeePage.getTotalElements());
//        System.out.println("目前是第幾頁：" + employeePage.getNumber());
//        System.out.println("每頁顯示的記錄數：" + employeePage.getSize());
//        List<Employee> employees = employeePage.getContent();
//        for (Employee employee : employees) {
//            System.out.println("員工ID：" + employee.getEmpno());
//            System.out.println("員工姓名：" + employee.getEmpName());
//            System.out.println("員工職位：" + employee.getPosition());
//            // 添加其他需要的員工詳細信息
//        }

        
    
     // 將分頁相關資訊加入到Model中
        model.addAttribute("employeesPage", employeePage);
        model.addAttribute("currentPage", employeePage.getNumber()); // 目前頁碼
        model.addAttribute("totalPages", employeePage.getTotalPages()); // 總頁數
        model.addAttribute("totalItems", employeePage.getTotalElements()); // 總記錄數
        System.out.println("測試點indexlistAllEmployee");
        // 返回視圖
        return "back-end/employee/listAllEmployee";
    }

    
    
//    //原版可以跑得authorityfunction
//    @GetMapping("/authorityfunction/listAllAuthorityFunction")
//    public String listAllAuthorityFunction(Model model) {
//    	Employee employee = new Employee();
//    	Position position = new Position();
//    	AuthorityFunction authorityFunction = new AuthorityFunction();
//		model.addAttribute("employee", employee);
//		model.addAttribute("position", position);
//		model.addAttribute("authorityFunction", authorityFunction);
//		
//		System.out.println("測試點index_listAllAuthorityFunction");
//        return "back-end/authorityfunction/listAllAuthorityFunction"; //view
//    }
    
    

  @GetMapping("/authorityfunction/listAllAuthorityFunction")
  public String listAllAuthorityFunction(@RequestParam(defaultValue = "0") int page,Model model) {
	   // 在這裡執行你的分頁邏輯
  
  	AuthorityFunction authorityFunction = new AuthorityFunction();
		model.addAttribute("authorityFunction", authorityFunction);
        int size = 10; // 預設每頁大小
//      這邊可以理解為查詢第幾頁，而一頁有幾筆資料
      Pageable pageable = PageRequest.of(page, size);
      Page<AuthorityFunction> authorityFunction1 = authorityFunctionService.findAll(pageable);
      // 將分頁相關資訊加入到Model中
      model.addAttribute("authorityFunctionPage", authorityFunction1);
      model.addAttribute("currentPage", authorityFunction1.getNumber()); // 目前頁碼
      model.addAttribute("totalPages", authorityFunction1.getTotalPages()); // 總頁數
      model.addAttribute("totalItems", authorityFunction1.getTotalElements()); // 總記錄數
		
		
		
		System.out.println("測試點index_listAllAuthorityFunction");
      return "back-end/authorityfunction/listAllAuthorityFunction"; //view
  }
    
    
//    @GetMapping("/authorityfunction/listAllAuthorityFunction")
//    public String listAllAuthorityFunction(Model model) {
//        Employee employee = new Employee();
//        Position position = new Position();
//        AuthorityFunction authorityFunction = new AuthorityFunction();
//
//        model.addAttribute("employee", employee);
//        model.addAttribute("position", position);
//        model.addAttribute("authorityFunction", authorityFunction);
//
//        // 加載所有功能權限數據到前端
//        List<AuthorityFunction> allFunctions = authorityFunctionService.getAll();
//        model.addAttribute("allFunctions", allFunctions);
//
//        System.out.println("測試點index_listAllAuthorityFunction");
//        return "back-end/authorityfunction/listAllAuthorityFunction"; // 確保這是正確的視圖名稱
//    }

  
  
  
  @GetMapping("/employee/loginEmployeeFile")
  public String loginEmployeeFile(Model model) {
 
		
		System.out.println("測試點index_loginEmployeeFile");
      return "back-end/employee/loginEmployeeFile"; //view
  }
  
  
  
  
    
    @PostMapping("/employee/listOneEmployee")
    public String listOneEmployee(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點index_listOneEmployee");
        return "back-end/employee/updateEmployee"; //view
    }
    
    
    
    
    @GetMapping("/employee/listEmployeeByCompositeQuery")
    public String listEmployeeByCompositeQuery(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點index_listEmployeeByCompositeQuery複合查詢返回結果");
        return "back-end/employee/listEmployeeByCompositeQuery"; //view
    }
    
    
    
    
    @GetMapping("/employee/addEmployee")
    public String addEmployee(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點indexaddEmployee");
        return "back-end/employee/addEmployee"; //view
    }
    
    @GetMapping("/employee/selectEmployeePage")
    public String selectPageEmployee(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點index_selectEmployeePage");
        return "back-end/employee/selectEmployeePage"; //view
    }
    
    
    
    @GetMapping("/positions/positionAdd")
    public String positionAdd(Model model) {
    	Employee employee = new Employee();
    	Position position = new Position();
		model.addAttribute("employee", employee);
		model.addAttribute("position", position);
		
		System.out.println("測試點index_positionAdd");
        return "back-end/positions/positionAdd"; //view
    }
    
    
    @GetMapping("/positionauthority/addPositionAuthority")
    public String addPositionAuthority(Model model) {
    	
    	Position position = new Position();
    	AuthorityFunction authorityFunction = new AuthorityFunction();
		model.addAttribute("position", position);
		model.addAttribute("authorityFunction", authorityFunction);
		
		System.out.println("測試點index_addPositionAuthority");
        return "back-end/positionauthority/addPositionAuthority"; //view
    }
    
    
    
    
    @GetMapping("/positionauthority/updatePositionAuthority")
    public String updatePositionAuthority(Model model) {
    	
    	Position position = new Position();
    	AuthorityFunction authorityFunction = new AuthorityFunction();
		model.addAttribute("position", position);
		model.addAttribute("authorityFunction", authorityFunction);
		
		System.out.println("測試點index_updatePositionAuthority");
        return "back-end/positionauthority/updatePositionAuthority"; //view
    }
 
    @GetMapping("/positionauthority/allPositionAuthority")
    public String allPositionAuthority(Model model) {
    	
    	Position position = new Position();
    	AuthorityFunction authorityFunction = new AuthorityFunction();
		model.addAttribute("position", position);
		model.addAttribute("authorityFunction", authorityFunction);
		
		System.out.println("測試點index_allPositionAuthority");
        return "back-end/positionauthority/allPositionAuthority"; //view
    }
    
    
    
    
    
    /* @ModelAttribute 只要訪問這個contoller就會去執行該標註@ModelAttribute的方法，並且將執行完的結果已一個key儲存
     * 當您使用 @ModelAttribute 注解在 Spring MVC 的控制器（Controller）方法上，您實際上是在方法執行後將返回的對象添加到了 Spring MVC 的模型（Model）中。
     * 這個模型會存儲數據，並可在返回的視圖（view）中使用這些數據。
     * 
     * 
     */
    @ModelAttribute("empListData")  
   	public List<Employee> referenceListData(Model model) {
   		
       	List<Employee> list = employeeService.getAll();
   		return list;
   	}
    
    
    @ModelAttribute("positionListData")  
   	public List<Position> referenceListDataPosition(Model model) {
   		
       	List<Position> list = positionService.findAllPositions();
   		return list;
   	}
    
    
    @ModelAttribute("positionListDataIncludeEmpCount")  
   	public List<Map<String,Object>> positionListDataIncludeEmpCount(Model model) {
   		
    	List<Map<String,Object>> list = positionService.getAllPositions();
   		return list;
   	}
    
    
    @ModelAttribute("authorityFunctionListData")  
   	public List<AuthorityFunction> referenceAuthorityFunctionListData(Model model) {
   		
    	List<AuthorityFunction> list = authorityFunctionService.getAll();
   		return list;
   	}
    
    
    @ModelAttribute("positionAuthorityListDat")  
   	public List<PositionAuthority> referencePositionAuthorityListData(Model model) {
    	List<PositionAuthority> list = positionAuthorityService.getAllPositionAuthoritiesSorted();
   		return list;
   	}
	
	/*
	 * +-----------------------------------------------------------+
	 * |.........................其他...............................|
	 * +-----------------------------------------------------------+
	 */
    
	@Autowired
	NewsListService newsListSvc;
    
    @ModelAttribute("newsListListData") // for select_page.html 第135行用
	protected List<NewsList> referenceListData_NewsList(Model model) {
		model.addAttribute("newsList", new NewsList()); // for select_page.html 第133行用
		List<NewsList> list = newsListSvc.getAll();
		return list;
		}

	  @ModelAttribute("newsList")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
		protected List<NewsList> referencenewsListListData(Model model) {	
    	List<NewsList> list = newsListSvc.getAll();
		return list;
		}

	  	@GetMapping("/newslist/newslistselect")
 		public String NewsListSelect(Model model) {
	  	System.out.println("測試點0514");
 		return "back-end/newslist/newslistselect";		
	  	}
     
//	   	@GetMapping("/newslist/addNewsList")
//	  	public String addNewsList(Model model) {
//		 System.out.println("測試點0515");
//	  	return "back-end/newslist/addNewsList";
//	  	}
     
	    @GetMapping("/newslist/listAllNewsList")
	   	public String listAllNewsList(Model model) {
	   	return "back-end/newslist/listAllNewsList";
	   	}
     
	    @GetMapping("/newslist/updateNewsList")
	    public String updateNewsList(Model model) {
	    return "back-end/newslist/updateNewsList";
	    }
}

//======================================== 前台頁面 ======================================== //
