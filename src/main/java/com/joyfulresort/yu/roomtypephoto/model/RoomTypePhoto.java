package com.joyfulresort.yu.roomtypephoto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.joyfulresort.yu.roomtype.model.RoomType;

@Entity
@Table(name = "room_type_photo")
public class RoomTypePhoto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_type_photo_id", updatable = false)
	private Integer roomTypePhotoId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id")
	private RoomType roomType;
	
	@NotNull(message="房型圖片: 請選擇")
	@Column(name = "room_type_photo", columnDefinition = "longblob")
	private byte[] roomTypePhoto;
	
	@Column(name = "room_type_photo_state",columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean roomTypePhotoState;

	public RoomTypePhoto() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}
	
	public Integer getRoomTypePhotoId() {
		return roomTypePhotoId;
	}

	public void setRoomTypePhotoId(Integer roomTypePhotoId) {
		this.roomTypePhotoId = roomTypePhotoId;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public byte[] getRoomTypePhoto() {
		return roomTypePhoto;
	}

	public void setRoomTypePhoto(byte[] roomTypePhoto) {
		this.roomTypePhoto = roomTypePhoto;
	}

	public Boolean getRoomTypePhotoState() {
		return roomTypePhotoState;
	}

	public void setRoomTypePhotoState(Boolean roomTypePhotoState) {
		this.roomTypePhotoState = roomTypePhotoState;
	}
}
