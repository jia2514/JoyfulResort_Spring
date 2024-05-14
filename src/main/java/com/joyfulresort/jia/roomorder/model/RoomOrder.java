package com.joyfulresort.jia.roomorder.model;

import java.sql.Date;
import java.sql.Timestamp;
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

import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;



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
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
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
				+ refundState + ", roomOrderItems=" + roomOrderItems + "]";
	}


	

	
//	@NotEmpty(message="員工姓名: 請勿空白")
//	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
//	
//	@NotEmpty(message="員工職位: 請勿空白")
//	@Size(min=2,max=10,message="員工職位: 長度必需在{min}到{max}之間")
	
//	@NotNull(message="雇用日期: 請勿空白")	
//	@Future(message="日期必須是在今日(不含)之後")
//	@Past(message="日期必須是在今日(含)之前")
//	@DateTimeFormat(pattern="yyyy-MM-dd") 
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	

	
//	@NotNull(message="員工薪水: 請勿空白")
//	@DecimalMin(value = "10000.00", message = "員工薪水: 不能小於{value}")
//	@DecimalMax(value = "99999.99", message = "員工薪水: 不能超過{value}")
//	
//
//	
//	@NotNull(message="員工獎金: 請勿空白")
//	@DecimalMin(value = "1.00", message = "員工獎金: 不能小於{value}")
//	@DecimalMax(value = "99999.99", message = "員工獎金: 不能超過{value}")
	
	
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
	
}
