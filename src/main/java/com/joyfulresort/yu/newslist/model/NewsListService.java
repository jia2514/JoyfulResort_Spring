package com.joyfulresort.yu.newslist.model;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("newsListService")
public class NewsListService {

		@Autowired
		NewsListRepository repository;
		
		@Autowired
	    private SessionFactory sessionFactory;
		
		public NewsListService(NewsListRepository repository) {
			this.repository = repository;
		}
		
		public NewsList addNewsList(NewsList newsList) {
			repository.save(newsList);
			return newsList;
		}
		

		public NewsList updateNewsList(NewsList newsList) {
			repository.save(newsList);
			return newsList;
		}
		
		public List<NewsList> getAll() {
			return repository.findAll();
		}
		
		public NewsList getOneNewsList(Integer newsListId) {
			Optional<NewsList> optional = repository.findById(newsListId);
//			return optional.get();
			return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
		}
	}

