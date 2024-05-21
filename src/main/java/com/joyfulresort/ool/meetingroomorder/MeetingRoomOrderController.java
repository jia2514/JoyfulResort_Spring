package com.joyfulresort.ool.meetingroomorder;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.he.member.model.MemberRepository;
import com.joyfulresort.he.member.model.MemberVO;
import com.joyfulresort.ool.meetingroom.MeetingRoom;
import com.joyfulresort.ool.meetingroom.MeetingRoomRepository;

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

    @GetMapping("/member/order/{memberId}")
    public String getMemberOrder(@PathVariable Integer memberId, Model model){
        List<MeetingRoomOrder> memberOrders = meetingRoomOrderRepository.findByMemberMemberId(memberId);
        model.addAttribute("memberOrders", memberOrders);

        return "front-end/conference/member_order";
    }


//    @GetMapping("/conference/backend/order/meetingRoomOrder")
//    public String getMeetingRoomOrder(@RequestParam("meetingRoomId") Integer meetingRoomId, Model model){
//        List<MeetingRoomOrder> meetingRoomOrders = meetingRoomOrderRepository.findByMeetingRoomMeetingRoomId(meetingRoomId);
//        model.addAttribute("meetingRoomsOrders", meetingRoomOrders);
//
//        return "backend/meetingroom_order";
//    }

//    @GetMapping("/calendar/{meetingRoomId}")
//    public String calendar(Model model,@PathVariable Integer meetingRoomId){
//        List<MeetingRoomOrder> orders = meetingRoomOrderRepository.findByMeetingRoomMeetingRoomId(meetingRoomId);
//        model.addAttribute("orders", orders);
//        return "backend/calendar";
//    }


//    @GetMapping("/order/add")
//    public String add(Model model) {
//        model.addAttribute("meetingRoomOrder", new MeetingRoomOrder());
//        return "backend/add_order";
//    }

    @PostMapping("/conference/order/add")
    public String addOrder(@RequestParam("selectedDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd") List<Date> selectedDateTime,
                           @RequestParam("meetingRoomId") List<Integer> meetingRoomId) throws ParseException {
        if (selectedDateTime.size() != meetingRoomId.size()) {
            throw new IllegalArgumentException("Date and Room IDs must have the same number of elements.");
        }
        for (int i = 0; i < selectedDateTime.size(); i++) {
            MeetingRoomOrder meetingRoomOrder = new MeetingRoomOrder();
            MemberVO member = new MemberVO();
            Integer memberId = (int) (Math.random() * 10 + 1);
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

//    @PostMapping("/order/edit/{memberId}")
//    public String editOrder(@PathVariable Integer memberId, Model model){
//        MeetingRoomOrder meetingRoomOrder = (MeetingRoomOrder) meetingRoomOrderRepository.findByMeetingRoomMeetingRoomId(memberId);
//        meetingRoomOrder.setOrderState(meetingRoomOrder.getOrderState());ti
//        meetingRoomOrderRepository.save(meetingRoomOrder);
//        return "redirect:/conference/backend/order";
//    }

    @GetMapping("/conference/backend/order/delete/{meetingRoomOrderId}")
    public String deleteOrder(@PathVariable("meetingRoomOrderId") Integer meetingRoomOrderId){
        meetingRoomOrderRepository.deleteById(meetingRoomOrderId);
        return "redirect:/conference/backend/order";
    }
    
}
