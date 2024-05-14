package com.joyfulresort.fun.emp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.fun.emp.model.Employee;
import com.joyfulresort.fun.emp.model.EmployeeService;
import com.joyfulresort.fun.position.model.Position;
import com.joyfulresort.fun.position.model.PositionService;


@Controller("joyfulresortempcontroller")
@RequestMapping("/empController")
public class EmpController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	PositionService positionService;

	@PostMapping("insert") // Spring 將會根據 Employee 類中的驗證標記（例如 @NotNull、@Size 等）來進行驗證，並將驗證結果存儲在
							// BindingResult 對象中，以便你在控制器中進行後續處理。
	public String insert(@Valid Employee employee, BindingResult result, ModelMap model,
			@RequestParam("image") MultipartFile[] parts) throws IOException {
		System.out.println("測試點1");
		/*
		 * 捕捉錯誤：BindingResult 包含了數據綁定（例如將表單輸入轉換成對象）和數據驗證的所有錯誤。如果有錯誤發生，這些錯誤信息會被儲存於
		 * BindingResult 中。
		 * 
		 */

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*
		 * 
		 * 在處理文件上傳或特定類型的數據時，你可能不想讓自動驗證機制的錯誤阻礙進程。例如，當使用 Spring MVC
		 * 進行文件上傳時，如果檔案的類型或大小不符合要求，通常會產生錯誤。然而，在某些業務邏輯中，即使圖片有問題（例如格式不支援，或暫時沒有圖片），
		 * 你也可能想要允許用戶繼續其他資料的輸入。 這時，你可以移除這個字段的錯誤，避免它影響整個表單的提交。
		 */
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(employee, result, "image");

		for (MultipartFile multipartFile : parts) {
			byte[] buf = multipartFile.getBytes();
			employee.setImage(buf);
		}

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				employee.setImage(buf);
			}
		}

		if (result.hasErrors() || parts[0].isEmpty()) {
			List<Position> list = positionService.findAllPositions();
			model.addAttribute("positionListData", list);
			System.out.println("測試點新增員工驗證有誤返回頁面");
			return "back-end/employee/addEmployee"; // 返回到addEmployee.html
		}

		/*************************** 2.開始新增資料 *****************************************/

		employeeService.addEmp(employee);
		System.out.println("在資料庫成功新增員工");
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<Employee> list = employeeService.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success", "- (新增成功)");
		System.out.println("新增成功");
		return "redirect:/employee/listAllEmployee"; // 新增成功後重導至IndexController_inSpringBoot.java的第41行@GetMapping("/employee/listAllEmployee")

	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(Employee employee, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(employee, "employee");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}


	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("selectedEmpNo") String empno, ModelMap model) {
		;
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		Employee employee = employeeService.getOneEmp(Integer.valueOf(empno));
		System.out.println("getOne_For_Update測試點");
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		 添加職位列表到 Model 中
		model.addAttribute("employee", employee);
		List<Position> positionList = positionService.findAllPositions();
		model.addAttribute("positionListData", positionList);
		return "back-end/employee/updateEmployee"; // 查詢完成後轉交updateEmployee.html
	}

//	@PostMapping("update")
//	public String update(@Valid Employee employee, BindingResult result, ModelMap model,
//			@RequestParam("image") MultipartFile[] parts) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(employee, result, "image");
//
//		// 手動檢查 empAccount
//		if (employee.getEmpAccount() == null || !employee.getEmpAccount().toString().matches("^[0-9]+$")) {
//			result.rejectValue("empAccount", "empAccount.invalid", "員工帳號只能包含數字且不能為空");
//		}
//
//
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] image = employeeService.getOneEmp(employee.getEmpno()).getImage();
//			employee.setImage(image);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				employee.setImage(upFiles);
//			}
//		}
//		if (result.hasErrors()) {
//			List<Position> positionList = positionService.findAllPositions();
//			model.addAttribute("positionListData", positionList);
//			return "back-end/employee/updateEmployee";
//		}
//		/*************************** 2.開始修改資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		employeeService.updateEmp(employee);
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "- (修改成功)");
//		employee = employeeService.getOneEmp(Integer.valueOf(employee.getEmpno()));
//		model.addAttribute("employee", employee);
//		return "back-end/employee/listOneEmployee"; // 修改成功後轉交listOneEmp.html
//	}

	
	
	
	@PostMapping("update")
	public String update(@Valid Employee employee, BindingResult result, ModelMap model,
			@RequestParam("image") MultipartFile[] parts, HttpServletRequest request) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(employee, result, "image");

		// 手動檢查 empAccount
		if (employee.getEmpAccount() == null || !employee.getEmpAccount().toString().matches("^[0-9]+$")) {
			result.rejectValue("empAccount", "empAccount.invalid", "員工帳號只能包含數字且不能為空");
		}



		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] image = employeeService.getOneEmp(employee.getEmpno()).getImage();
			employee.setImage(image);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				employee.setImage(upFiles);
			}
		}
		if (result.hasErrors()) {
			List<Position> positionList = positionService.findAllPositions();
			model.addAttribute("positionListData", positionList);
			return "back-end/employee/updateEmployee";
		}
		/*************************** 2.開始修改資料 *****************************************/
		//存入員工資料到資料庫內
		employeeService.updateEmp(employee);
		
		
		HttpSession session = request.getSession();
        Employee sessionEmployee = (Employee) session.getAttribute("employees");

        // 檢查正在更新的員工是否是登入的員工
        if (sessionEmployee != null && sessionEmployee.getEmpno().equals(employee.getEmpno())) {
            // 重新從資料庫獲取更新後的員工資料
            Employee updatedEmployee = employeeService.getOneEmp(employee.getEmpno());
            // 更新 session 中的員工資料
            session.setAttribute("employees", updatedEmployee);
        }
        
        
       

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		employee = employeeService.getOneEmp(Integer.valueOf(employee.getEmpno()));
		model.addAttribute("employee", employee);
		return "back-end/employee/listOneEmployee"; // 修改成功後轉交listOneEmp.html
	}
	
	
	
	
	//複合查詢測試
	@PostMapping("listEmps_ByCompositeQuery")
	public String listAllEmp(HttpServletRequest req, Model model) {
		System.out.println("新增1");
		Map<String, String[]> map = req.getParameterMap();
		System.out.println("新增2");
		List<Employee> list = employeeService.getAll(map);
		System.out.println("新增3");
		System.out.println(list);
		model.addAttribute("empListData", list); // for listAllEmp.html 第85行用
		System.out.println("新增4");
		return "back-end/employee/listEmployeeByCompositeQuery";
	}
	
	



}
