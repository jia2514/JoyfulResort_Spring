package com.joyfulresort.yu.newslist.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


	@Entity
	@Table(name = "news_list")
	public class NewsList implements java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "news_id", updatable = false)
		private Integer newsId;
		

		@NotNull(message="媒體報導標題:請勿空白")
		@Column(name = "news_title")
		private String newsTitle;
	
		
		@NotNull(message="媒體報導日期: 請勿空白")
		@Column(name ="news_date")
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date newsDate;
	
		
		@NotNull(message="媒體報導摘要: 請勿空白")
		@Column(name = "news_abstract")
		private String newsAbstract;
		
	
		@NotNull(message="媒體報導內容: 請勿空白")
		@Column(name = "news_content")
		private String newsContent;
	
		
		@NotNull(message="媒體報導圖片: 請選擇")
		@Column(name = "news_photo", columnDefinition = "longblob")
		private byte[] newsPhoto;
	
		
		@NotNull(message="媒體報導出處: 請勿空白")
		@Column(name = "news_from")
		private String newsFrom;
		
		
		@Column(name = "news_state",columnDefinition = "BOOLEAN DEFAULT TRUE")
		private Boolean newsState;


		public Integer getNewsId() {
			return newsId;
		}


		public void setNewsId(Integer newsId) {
			this.newsId = newsId;
		}


		public String getNewsTitle() {
			return newsTitle;
		}


		public void setNewsTitle(String newsTitle) {
			this.newsTitle = newsTitle;
		}


		public Date getNewsDate() {
			return newsDate;
		}


		public void setNewsDate(Date newsDate) {
			this.newsDate = newsDate;
		}


		public String getNewsAbstract() {
			return newsAbstract;
		}


		public void setNewsAbstract(String newsAbstract) {
			this.newsAbstract = newsAbstract;
		}


		public String getNewsContent() {
			return newsContent;
		}


		public void setNewsContent(String newsContent) {
			this.newsContent = newsContent;
		}


		public byte[] getNewsPhoto() {
			return newsPhoto;
		}


		public void setNewsPhoto(byte[] newsPhoto) {
			this.newsPhoto = newsPhoto;
		}


		public String getNewsFrom() {
			return newsFrom;
		}


		public void setNewsFrom(String newsFrom) {
			this.newsFrom = newsFrom;
		}


		public Boolean getNewsState() {
			return newsState;
		}


		public void setNewsState(Boolean newsState) {
			this.newsState = newsState;
		}

	
	}
		