package com.joyfulresort.ool.cart;

import com.joyfulresort.ool.meetingroom.MeetingRoom;
import com.joyfulresort.ool.meetingroom.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private Cart cart;

    @PostMapping("/add")
    @ResponseBody
    public List<CartItem> addToCart(@RequestParam Integer meetingRoomId, @RequestParam@DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate, @RequestParam int quantity) {
        if (quantity != 1) {
            throw new IllegalArgumentException("Quantity must be 1");
        }
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId).orElseThrow(() -> new IllegalArgumentException("Invalid meeting room ID"));
        CartItem item = new CartItem();
        item.setCartId(UUID.randomUUID().toString());
        item.setMeetingRoomName(meetingRoom.getMeetingRoomName());
        item.setBookingDate(bookingDate);
        item.setQuantity(quantity);
        item.setMeetingRoomPrice(meetingRoom.getMeetingRoomPrice());
        cart.addItem(item);
        return cart.getItems();
    }

    @PostMapping("/remove")
    @ResponseBody
    public List<CartItem> removeFromCart(@RequestParam String cartId) {
        cart.removeItem(cartId);
        return cart.getItems();
    }

    @GetMapping("/items")
    @ResponseBody
    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    @GetMapping("/total")
    @ResponseBody
    public Integer getCartTotal() {
        return cart.getTotalPrice();
    }

    @PostMapping("/clear")
    @ResponseBody
    public void clearCart(){
        cart.clear();
    }
}
