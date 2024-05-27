package com.qalist.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


	@Entity
	@Table(name = "qa_list")
	public class QaList implements java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "qa_id", updatable = false)
		private Integer qaId;
		

		@NotEmpty(message="關鍵字:請勿空白")
		@Column(name = "key_word")
		private String keyWord;
	
		
		@NotNull(message="常見問題答覆: 請勿空白")
		@Column(name ="qa_answer")
		private String qaAnswer;


		public Integer getQaId() {
			return qaId;
		}


		public void setQaId(Integer qaId) {
			this.qaId = qaId;
		}


		public String getKeyWord() {
			return keyWord;
		}


		public void setKeyWord(String keyWord) {
			this.keyWord = keyWord;
		}


		public String getQaAnswer() {
			return qaAnswer;
		}


		public void setQaAnswer(String qaAnswer) {
			this.qaAnswer = qaAnswer;
		}	
		
		
}
