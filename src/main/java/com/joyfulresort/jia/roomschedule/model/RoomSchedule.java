package com.joyfulresort.jia.roomschedule.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.yu.roomtype.model.RoomType;


@Component
@Entity
@Table(name = "room_schedule")
public class RoomSchedule {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_schedule_id", updatable = false)
	private Integer roomSchedule;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id")
	private RoomType roomType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_order_item_id", referencedColumnName = "room_order_item_id")
	private RoomOrderItem roomOrderItem;
	
	@Column(name = "room_schedule_date")
	private Date roomScheduleDate;
	
	
	public RoomSchedule() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getRoomSchedule() {
		return roomSchedule;
	}

	public void setRoomSchedule(Integer roomSchedule) {
		this.roomSchedule = roomSchedule;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Date getRoomScheduleDate() {
		return roomScheduleDate;
	}

	public void setRoomScheduleDate(Date roomScheduleDate) {
		this.roomScheduleDate = roomScheduleDate;
	}

	
	

	public RoomOrderItem getRoomOrderItem() {
		return roomOrderItem;
	}

	public void setRoomOderItem(RoomOrderItem roomOrderItem) {
		this.roomOrderItem = roomOrderItem;
	}

	@Override
	public String toString() {
		return "RoomSchedule [roomSchedule=" + roomSchedule + ", roomScheduleDate=" + roomScheduleDate + "]";
	}
	
	
	
}
