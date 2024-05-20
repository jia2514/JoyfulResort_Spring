package com.joyfulresort.ool.cart;

import java.util.Date;

public class CartItem {
    private String cartId;
    private String meetingRoomName;

    private Date bookingDate;
    private int quantity;
    private Integer meetingRoomPrice;

    public CartItem() {
    }


    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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
