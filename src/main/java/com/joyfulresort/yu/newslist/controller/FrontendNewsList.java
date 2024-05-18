package com.joyfulresort.yu.newslist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joyfulresort.yu.newslist.model.NewsList;
import com.joyfulresort.yu.newslist.model.NewsListService;

@Controller
@RequestMapping("/joyfulresort/newslist")
public class FrontendNewsList {

		
		@Autowired
		NewsListService newsListSvc;
		
		
		@GetMapping("getAll")
		public String getAll(ModelMap model) {
			List<NewsList> newsList = newsListSvc.getAll();
			model.addAttribute("newsList",newsList);
			System.out.println("測試點1");
			return "front-end/newslist/listAllNewsLists";
		}
}
