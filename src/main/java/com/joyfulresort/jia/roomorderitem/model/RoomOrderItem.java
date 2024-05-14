package com.joyfulresort.jia.roomorderitem.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.roomtype.model.RoomType;

@Component
@Entity
@Table(name = "room_order_item")
public class RoomOrderItem {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_order_item_id", updatable = false)
	private Integer roomOrderItemId;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", referencedColumnName = "room_id")
	private Room room;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_order_id", referencedColumnName = "room_order_id")
	private RoomOrder roomOrder;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id")
	private RoomType roomType;

	@Column(name = "room_guest_name")
	private String roomGuestName;

	@Column(name = "room_guest_phone")
	private String roomGuestPhone;

	@Column(name = "number_of_people")
	private Integer numberOfPeople;

	@Column(name = "special_req")
	private String specialREQ;

	@Column(name = "room_price")
	private Integer roomPrice;
	
	public Byte getRoomOrderItemState() {
		return roomOrderItemState;
	}

	public void setRoomOrderItemState(Byte roomOrderItemState) {
		this.roomOrderItemState = roomOrderItemState;
	}

	@Column(name = "room_order_item_state" ,nullable = false, columnDefinition = "int default 0")
	private Byte roomOrderItemState = 0;

	
	@OneToMany(mappedBy = "roomOrderItem", cascade = CascadeType.ALL)
	@OrderBy("room_schedule_id asc")
	private Set<RoomSchedule> roomSchedules;
	
	

	public RoomOrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getRoomOrderItemId() {
		return roomOrderItemId;
	}

	public void setRoomOrderItemId(Integer roomOrderItemId) {
		this.roomOrderItemId = roomOrderItemId;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public RoomOrder getRoomOrder() {
		return roomOrder;
	}

	public void setRoomOrder(RoomOrder roomOrder) {
		this.roomOrder = roomOrder;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public String getRoomGuestName() {
		return roomGuestName;
	}

	public void setRoomGuestName(String roomGuestName) {
		this.roomGuestName = roomGuestName;
	}

	public String getRoomGuestPhone() {
		return roomGuestPhone;
	}

	public void setRoomGuestPhone(String roomGuestPhone) {
		this.roomGuestPhone = roomGuestPhone;
	}

	public Integer getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(Integer numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public String getSpecialREQ() {
		return specialREQ;
	}

	public void setSpecialREQ(String specialREQ) {
		this.specialREQ = specialREQ;
	}

	public Integer getRoomPrice() {
		return roomPrice;
	}

	public void setRoomPrice(Integer roomPrice) {
		this.roomPrice = roomPrice;
	}
	
	

	public Set<RoomSchedule> getRoomSchedules() {
		return roomSchedules;
	}

	public void setRoomSchedules(Set<RoomSchedule> roomSchedules) {
		this.roomSchedules = roomSchedules;
	}

	@Override
	public String toString() {
		return "RoomOrderItem [roomOrderItemId=" + roomOrderItemId + ", roomGuestName=" + roomGuestName
				+ ", roomGuestPhone=" + roomGuestPhone + ", numberOfPeople=" + numberOfPeople + ", specialREQ="
				+ specialREQ + ", roomPrice=" + roomPrice + ", roomOrderItemState=" + roomOrderItemState  +  ", roomSchedules=" + roomSchedules + "]";
	}

	
	
	
}
