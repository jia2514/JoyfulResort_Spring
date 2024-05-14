package com.joyfulresort.ool.meetingroomorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.ool.meetingroom.MeetingRoom;

@Entity
@Table(name = "meetingroom_order")
public class MeetingRoomOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetingroom_order_id")
    private Integer meetingRoomOrderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "meetingroom_order_date")
    private Date meetingRoomOrderDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(name = "order_state")
    private Integer orderState = 1;

    @Column(name = "refund_state")
    private Integer refundState = 0;

    @Column(name = "order_note")
    private String orderNote;

    @ManyToOne
    @JoinColumn(name = "meetingroom_id", referencedColumnName = "meetingroom_id")
    private MeetingRoom meetingRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberVO member;

    public Integer getMeetingRoomOrderId() {
        return meetingRoomOrderId;
    }

    public void setMeetingRoomOrderId(Integer meetingRoomOrderId) {
        this.meetingRoomOrderId = meetingRoomOrderId;
    }

    public Date getMeetingRoomOrderDate() {
        return meetingRoomOrderDate;
    }

    public void setMeetingRoomOrderDate(Date meetingRoomOrderDate) {
        this.meetingRoomOrderDate = meetingRoomOrderDate;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public MemberVO getMember() {
        return member;
    }

    public void setMember(MemberVO member) {
        this.member = member;
    }
}
