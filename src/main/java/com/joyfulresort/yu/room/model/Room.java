package com.joyfulresort.yu.room.model;

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

import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.yu.roomtype.model.RoomType;

@Component
@Entity
@Table(name = "room")
public class Room implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id", updatable = false)
	private Integer roomId;
	
    // 多對一關係，多個 Room 指向一個 RoomType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id")
	private RoomType roomType;
	
	@Column(name = "room_sale_state",columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean roomSaleState=true;
	
    @Column(name = "room_state")
	private Byte roomState=0;
	
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	@OrderBy("room_order_item_id asc")
	private Set<RoomOrderItem> roomOrderItem;

	public Room() { //必需有一個不傳參數建構子(JavaBean基本知識)
	}
	
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Boolean getRoomSaleState() {
		return roomSaleState;
	}

	public void setRoomSaleState(Boolean roomSaleState) {
		this.roomSaleState = roomSaleState;
	}

	public Byte getRoomState() {
		return roomState;
	}

	public void setRoomState(Byte roomState) {
		this.roomState = roomState;
	}

	public Set<RoomOrderItem> getRoomOrderItem() {
		return roomOrderItem;
	}

	public void setRoomOrderItem(Set<RoomOrderItem> roomOrderItem) {
		this.roomOrderItem = roomOrderItem;
	}

	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", roomSaleState=" + roomSaleState + ", roomState=" + roomState
				+ ", roomOrderItem=" + roomOrderItem + "]";
	}
	
}
