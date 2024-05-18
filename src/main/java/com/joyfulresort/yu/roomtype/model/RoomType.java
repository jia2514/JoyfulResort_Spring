package com.joyfulresort.yu.roomtype.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Component;

import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;

//import groovyjarjarantlr4.v4.runtime.misc.NotNull;

//@Getter
//@Setter
@Component
@Entity
@Table(name = "room_type")
public class RoomType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_type_id", updatable = false)
	private Integer roomTypeId;
	
	@NotEmpty(message="房型名稱: 請勿空白")
	@NotBlank(message = "房型名稱: 請輸入文字")
	@Column(name = "room_type_name")
	private String roomTypeName;
	
	@NotNull(message="可入住人數: 請勿空白")
	@Column(name ="number_of_people")
	private Integer numberOfPeople;
	
	@NotNull(message="房型數量: 請勿空白")
	@Column(name = "room_type_amount")
	private Integer roomTypeAmount;
	
	@NotNull(message="房型說明: 請勿空白")
	@NotBlank(message = "房型說明: 請輸入文字")
	@Column(name = "room_type_content")
	private String roomTypeContent;
	
	
	@Column(name = "room_type_sale_state",columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean roomTypeSaleState;
	
	@NotNull(message="房型價格: 請勿空白")
	@Positive(message = "房型價格:必需為大於0的整數")
	@Column(name = "room_type_price")
	private Integer roomTypePrice;
	
	
	@OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
	@OrderBy("room_id asc")
	private Set<Room> room;
	
	@OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
	@OrderBy("room_order_item_id asc")
	private Set<RoomOrderItem> roomOrderItems;
	
	@OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
	@OrderBy("room_type_photo_id asc")
	private Set<RoomTypePhoto> roomTypePhotos;
	
	@OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
	@OrderBy("room_schedule_id asc")
	private Set<RoomSchedule> roomSchedules;
	
		
	
	public RoomType() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}

	public Integer getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Integer roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRoomTypeName() {
		return roomTypeName;
	}
	
	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}
	
	public Integer getRoomTypeAmount() {
		return roomTypeAmount;
	}
	public void setRoomTypeAmount(Integer roomTypeAmount) {
		this.roomTypeAmount = roomTypeAmount;
	}
	
	public Integer getNumberOfPeople() {
		return numberOfPeople;
	}
	
	public void setNumberOfPeople(Integer numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}


	
	public String getRoomTypeContent() {
		return roomTypeContent;
	}

	public void setRoomTypeContent(String roomTypeContent) {
		this.roomTypeContent = roomTypeContent;
	}

	public Boolean getRoomTypeSaleState() {
		return roomTypeSaleState;
	}

	public void setRoomTypeSaleState(Boolean roomTypeSaleState) {
		this.roomTypeSaleState = roomTypeSaleState;
	}

	public Integer getRoomTypePrice() {
		return roomTypePrice;
	}

	public void setRoomTypePrice(Integer roomTypePrice) {
		this.roomTypePrice = roomTypePrice;
	}

	public Set<Room> getRoom() {
		return room;
	}

	public void setRoom(Set<Room> room) {
		this.room = room;
	}

	public Set<RoomOrderItem> getroomOrderItems() {
		return roomOrderItems;
	}

	public void setRoomOrderItem(Set<RoomOrderItem> roomOrderItems) {
		this.roomOrderItems = roomOrderItems;
	}

	public Set<RoomTypePhoto> getRoomTypePhotos() {
		return roomTypePhotos;
	}

	public void setRoomTypePhoto(Set<RoomTypePhoto> roomTypePhotos) {
		this.roomTypePhotos = roomTypePhotos;
	}

	public Set<RoomSchedule> getRoomSchedules() {
		return roomSchedules;
	}

	public void setRoomSchedules(Set<RoomSchedule> roomSchedules) {
		this.roomSchedules = roomSchedules;
	}
	
	@Override
	public String toString() {
		return "RoomType [roomTypeId=" + roomTypeId + ", roomTypeName=" + roomTypeName + ", roomTypeContent="
				+ roomTypeContent + ", roomTypeSaleState=" + roomTypeSaleState + ", roomTypePrice=" + roomTypePrice
				+ ", room=" + room + ", roomOrderItems=" + roomOrderItems 
//				+ ", roomTypePhotos=" + roomTypePhotos
				+ ", roomSchedules=" + roomSchedules + "]";
	}
	
	
}
