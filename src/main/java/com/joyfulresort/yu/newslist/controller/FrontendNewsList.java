package com.joyfulresort.yu.newslist.controller;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.yu.newslist.model.NewsList;
import com.joyfulresort.yu.newslist.model.NewsListService;

@Controller
@Validated
@RequestMapping("/joyfulresort/newslist")
public class FrontendNewsList {

		
		@Autowired
		NewsListService newsListSvc;
		
		
		@GetMapping("newslistinfo")
		public String getAll(ModelMap model) {
			List<NewsList> newsList = newsListSvc.getAll();
			model.addAttribute("newsList",newsList);
			System.out.println("測試點1");
			return "front-end/newslist/listNewsLists";
		}
		
//		@RequestMapping("/newslist")
//		public class NewsListIdController {
			

			

			/*
			 * This method will be called on select_page.html form submission, handling POST
			 * request It also validates the user input
			 */
			@GetMapping("getOne_For_Display")
			public String getOne_For_Display(
				/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
				@NotEmpty(message="媒體報導編號: 請勿空白")
				@Digits(integer = 4, fraction = 0, message = "媒體報導: 請填數字-請勿超過{integer}位數")
				@Min(value = 1, message = "媒體報導編號: 不能小於{value}")
				@Max(value = 100, message = "媒體報導編號: 不能超過{value}")
				@RequestParam("newsId") String newsId,
				ModelMap model) {
				
				/***************************2.開始查詢資料*********************************************/
//				EmpService empSvc = new EmpService();
				NewsList newsList = newsListSvc.getOneNewsList(Integer.valueOf(newsId));
				
				List<NewsList> list = newsListSvc.getAll();
				model.addAttribute("newsListListData", list);     // for select_page.html 第97 109行用
				System.out.println("測試點222");

				
				if (newsList == null) {
					model.addAttribute("errorMessage", "查無資料");
					return "front-end/newslist/listNewsLists";
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
				model.addAttribute("newsList", newsList);
				System.out.println("測試點333");
				model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
				
				return "front-end/newslist/listOneNewsList";  // 查詢完成後轉交listOneEmp.html
//				return "back-end/emp/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
			}
}
