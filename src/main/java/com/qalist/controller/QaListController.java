package com.qalist.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.yu.newslist.model.NewsList;
import com.joyfulresort.yu.roomtype.model.RoomType;
import com.qalist.model.QaList;
import com.qalist.model.QaListService;

@Controller
@RequestMapping("/qalist")
public class QaListController {

	@Autowired
	QaListService qaListSvc;
	
	
//	 @GetMapping("/qalistselect")
//		public String QaListSelect(Model model) {
//	  	System.out.println("測試點0527");
//		return "back-end/qalist/qalistselect";		
//	  	}
	@GetMapping("getAll")
	public String getAll(ModelMap model) {
		List<QaList> qaList = qaListSvc.getAll();
		model.addAttribute("qaList", qaList);
		return "back-end/qalist/listAllQaLists";
	}
	

	
	@GetMapping("addQaList")
	public String addNewsList(ModelMap model) {
		QaList qaList = new QaList();
	    List<QaList> qaListListData = qaListSvc.getAll();  // 獲取所有房型資料
		System.out.println("測試點666");
	    model.addAttribute("qaListListData", qaListListData);
		model.addAttribute("qaList", qaList);
		return "back-end/qalist/addQaList";
	}
	
	@ModelAttribute("qaListData")
	protected List<QaList> referenceListData() {
		List<QaList> list = qaListSvc.getAll();
		return list;
	}
	
	@PostMapping("insert")
	public String insert(@Valid QaList qaList, BindingResult result, ModelMap model) throws IOException {
		System.out.println("測試點444");
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			System.out.println("資料有誤");
			return "back-end/qalist/addQaList";
		}
		/*************************** 2.開始新增資料 *****************************************/
		qaListSvc.addQaList(qaList);

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<QaList> list = qaListSvc.getAll();
		model.addAttribute("qaList", list);
		model.addAttribute("success", "- (新增成功)");
		return "back-end/qalist/listAllQaLists"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("qaId") String qaId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		System.out.println("測試點1");
		QaList qaList = qaListSvc.getOneQaList(Integer.valueOf(qaId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("qaList", qaList);
		System.out.println("測試點2");
		return "back-end/qalist/updateQaList"; // 查詢完成後轉交updateRoomType.html
		}
	
	
	@PostMapping("update")
	public String update(@Valid QaList qaList, BindingResult result, ModelMap model) throws IOException {
		System.out.println("測試點3");

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (result.hasErrors()) {
			System.out.println("資料不全");
			return "back-end/qalist/updateQaList";
		}
		/*************************** 2.開始修改資料 *****************************************/

		qaListSvc.updateQaList(qaList);
		System.out.println("修改成功555");
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		//model.addAttribute("success", "- (修改成功)");
		qaList = qaListSvc.getOneQaList(Integer.valueOf(qaList.getQaId()));
		model.addAttribute("qaList", qaList);
		return "back-end/qalist/listAllQaLists"; // 修改成功後轉交listAllRoomTypes.html
		}
}

