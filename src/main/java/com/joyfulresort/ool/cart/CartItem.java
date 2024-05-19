package com.joyfulresort.ool.cart;

import java.util.Date;

public class CartItem {
    private String meetingRoomName;

    private Date bookingDate;
    private int quantity;
    private Integer meetingRoomPrice;

    public CartItem() {
    }

    public CartItem(String meetingRoomName, Date bookingDate, int quantity, Integer meetingRoomPrice) {
        this.meetingRoomName = meetingRoomName;
        this.bookingDate = bookingDate;
        this.quantity = quantity;
        this.meetingRoomPrice = meetingRoomPrice;
    }

    public String getMeetingRoomName() {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getMeetingRoomPrice() {
        return meetingRoomPrice;
    }

    public void setMeetingRoomPrice(Integer meetingRoomPrice) {
        this.meetingRoomPrice = meetingRoomPrice;
    }
}
