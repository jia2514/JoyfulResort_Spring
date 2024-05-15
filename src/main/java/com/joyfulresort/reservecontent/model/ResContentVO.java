package com.joyfulresort.reservecontent.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "reserve_content")
public class ResContentVO implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotEmpty(message = "內容文字請勿空白!") 
	@Length(min = 20, max = 150, message = "文字長度限制在10到120字之間") 
	@Column(name = "reserve_text")
	private String reserveText;

	@Column(name = "reserve_image")
	private byte[] reserveImage;

	public ResContentVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReserveText() {
		return reserveText;
	}

	public void setReserveText(String reserveText) {
		this.reserveText = reserveText;
	}

	public byte[] getReserveImage() {
		return reserveImage;
	}

	public void setReserveImage(byte[] reserveImage) {
		this.reserveImage = reserveImage;
	}

	@Override
	public String toString() {
		return "ResContent [id=" + id + ", reserveText=" + reserveText + ", reserveImage="
				+ Arrays.toString(reserveImage) + "]";
	}

}
