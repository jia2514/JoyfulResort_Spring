package com.joyfulresort.yu.newslist.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.newslist.model.NewsList;
import com.joyfulresort.yu.newslist.model.NewsListService;

@Controller
@RequestMapping("/newslist")
public class NewsListController {
	
	@Autowired
	NewsListService newsListSvc;
	
	
	@GetMapping("getAll")
	public String getAll(ModelMap model) {
		List<NewsList> newsList = newsListSvc.getAll();
		model.addAttribute("newsList",newsList);
		System.out.println("測試點1");
		return "back-end/newslist/listAllNewsLists";
	}
	
	@GetMapping("addNewsList")
		public String addNewsList(ModelMap model) {
			NewsList newsList = new NewsList();
		    List<NewsList> newsListListData = newsListSvc.getAll();  // 獲取所有房型資料
		    model.addAttribute("newsListListData", newsListListData);
			model.addAttribute("newsList", newsList);
			return "back-end/newslist/addNewsList";
		}
	
	
	@ModelAttribute("NewsListData")
	protected List<NewsList> referenceListData() {
		List<NewsList> list = newsListSvc.getAll();
		return list;
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid NewsList newsList, BindingResult result, ModelMap model,
			@RequestParam("newsPhoto") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsList, result, "newsPhoto");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "媒體報導照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				newsList.setNewsPhoto(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/newslist/addNewsList";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// RoomTypePhotoService roomTypePhotoSvc = new RoomTypePhotoService();
		newsListSvc.addNewsList(newsList);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<NewsList> list = newsListSvc.getAll();
		model.addAttribute("newsList", list); //此為VO
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/newslist/getAll";  //新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	
	}



	/*
	 * This method will be called on listAllRoomTypePhotos.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("newsId") String newsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		NewsList newsList = newsListSvc.getOneNewsList(Integer.valueOf(newsId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsList", newsList);
		return "back-end/newslist/updateNewsList"; // 查詢完成後轉交updateNewsList.html
	}

	/*
	 * This method will be called on updateNewsList.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid NewsList newsList, BindingResult result, ModelMap model,
			@RequestParam("newsPhoto") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsList, result, "newsPhoto");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// RoomTypePhotoService roomTypePhotoSvc = new RoomTypePhotoService();
			byte[] newsPhoto =newsListSvc.getOneNewsList(newsList.getNewsId()).getNewsPhoto();
			newsList.setNewsPhoto(newsPhoto);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] newsPhoto = multipartFile.getBytes();
				newsList.setNewsPhoto(newsPhoto);
			}
		}
		if (result.hasErrors()) {
			return "back-end/newslist/updateNewsList";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// RoomTypePhotoService roomTypePhotoSvc = new RoomTypePhotoService();
		newsListSvc.updateNewsList(newsList);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		newsList = newsListSvc.getOneNewsList(Integer.valueOf(newsList.getNewsId()));
		model.addAttribute("newsList", newsList);
		return "back-end/newslist/listAllNewsLists"; // 修改成功後轉交listOneRoomTypePhoto.html
	}
	
	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(NewsList newsList, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(newsList, "newsList");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}



