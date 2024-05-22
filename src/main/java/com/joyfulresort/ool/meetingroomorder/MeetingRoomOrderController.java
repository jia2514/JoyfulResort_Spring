package com.joyfulresort.ool.meetingroomorder;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.joyfulresort.he.member.model.MemberRepository;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.ool.meetingroom.MeetingRoom;
import com.joyfulresort.ool.meetingroom.MeetingRoomRepository;

import javax.servlet.http.HttpServletRequest;

@Controller

public class MeetingRoomOrderController {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MeetingRoomOrderRepository meetingRoomOrderRepository;
    @GetMapping("/conference/backend/order")
    public String showOrderList(Model model) {
        List<MeetingRoomOrder> orders = meetingRoomOrderRepository.findAll();
        model.addAttribute("orders", orders);
        return "back-end/conference/order";
    }

    @GetMapping("/conference/order/pay")
    public String pay(){
        return "front-end/conference/pay";
    }




    @PostMapping("/conference/order/add")
    public String addOrder(@RequestParam("selectedDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd") List<Date> selectedDateTime,
                           @RequestParam("meetingRoomId") List<Integer> meetingRoomId,
                           @CookieValue("MemberID") Integer memberId) throws ParseException {
        if (selectedDateTime.size() != meetingRoomId.size()) {
            throw new IllegalArgumentException("Date and Room IDs must have the same number of elements.");
        }
        for (int i = 0; i < selectedDateTime.size(); i++) {
            MeetingRoomOrder meetingRoomOrder = new MeetingRoomOrder();
            MemberVO member = new MemberVO();
            member.setMemberId(memberId);
            MeetingRoom meetingRoom = new MeetingRoom();
            meetingRoom.setMeetingRoomId(meetingRoomId.get(i));
            meetingRoomOrder.setMember(member);
            meetingRoomOrder.setMeetingRoomOrderDate(new Date());
            meetingRoomOrder.setMeetingRoom(meetingRoom);
            meetingRoomOrder.setBookingDate(selectedDateTime.get(i));
            meetingRoomOrderRepository.save(meetingRoomOrder);

        }
        return "redirect:/conference/order";
    }

//

    @GetMapping("/conference/backend/order/delete/{meetingRoomOrderId}")
    public String deleteOrder(@PathVariable("meetingRoomOrderId") Integer meetingRoomOrderId){
        meetingRoomOrderRepository.deleteById(meetingRoomOrderId);
        return "redirect:/conference/backend/order";
    }
    
}
