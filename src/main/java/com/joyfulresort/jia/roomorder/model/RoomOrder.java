package com.joyfulresort.jia.roomorder.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;



/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */

@Component
@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "room_order") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class RoomOrder implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_order_id", updatable = false)
	private Integer roomOrderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	private MemberVO member;
	
//	@CreationTimestamp 這是Hibernate自動填入今日時間的設定
	@Column(name = "order_date")
	private Timestamp orderDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "room_order_state" ,nullable = false, columnDefinition = "int default 1")
	private Byte roomOrderState = 1;

	@Column(name = "check_in_date")
	private Date checkInDate;

	@Column(name = "check_out_date")
	private Date checkOutDate;

	@Column(name = "refund_state",nullable = false, columnDefinition = "int default 0")
	private Byte refundState = 0;

	@OneToMany(mappedBy = "roomOrder", cascade = CascadeType.ALL)
	@OrderBy("room_order_item_id asc")
	private Set<RoomOrderItem> roomOrderItems;

	@Column(name = "complete_datetime")
	private Timestamp completeDateTime;
	
	
	
	public Timestamp getCompleteDateTime() {
		return completeDateTime;
	}


	public void setCompleteDateTime(Timestamp completeDateTime) {
		this.completeDateTime = completeDateTime;
	}


	public RoomOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getRoomOrderId() {
		return roomOrderId;
	}

	public void setRoomOrderId(Integer roomOrderId) {
		this.roomOrderId = roomOrderId;
	}

	public MemberVO getMember() {
		return member;
	}

	public void setMember(MemberVO member) {
		this.member = member;
	}

	
	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Byte getRoomOrderState() {
		return roomOrderState;
	}

	public void setRoomOrderState(Byte roomOrderState) {
		this.roomOrderState = roomOrderState;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
		calculateBookingNight();
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
		calculateBookingNight();
	}

	public Byte getRefundState() {
		return refundState;
	}

	public void setRefundState(Byte refundState) {
		this.refundState = refundState;
	}

	public Set<RoomOrderItem> getRoomOrderItems() {
		return roomOrderItems;
	}

	public void setRoomOrderItems(Set<RoomOrderItem> roomOrderItems) {
		this.roomOrderItems = roomOrderItems;
	}

	
	@Override
	public String toString() {
		return "RoomOrder [roomOrderId=" + roomOrderId + ", orderDate=" + orderDate + ", roomOrderState="
				+ roomOrderState + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate + ", refundState="
				+ refundState + ", roomOrderItems=" + roomOrderItems + ", bookingNight=" + bookingNight + "]";
	}


//	------------------------------------------------------------------------------
	@Transient
	private Integer bookingNight;
	
	
    @PostLoad
    @PrePersist
    private void calculateBookingNight() {
        if (this.checkInDate != null && this.checkOutDate != null) {
            long diffInMillies = this.checkOutDate.getTime() - this.checkInDate.getTime();
            this.bookingNight = (int) (diffInMillies / (1000 * 60 * 60 * 24));
        }
    }

    public Integer getBookingNight() {
        return bookingNight;
    }

    
	
	
	
}
