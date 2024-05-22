package com.joyfulresort.ool.meetingroom;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrder;
import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrderRepository;
import com.joyfulresort.ool.meetingroomphoto.MeetingRoomPhoto;
import com.joyfulresort.ool.meetingroomphoto.MeetingRoomPhotoRepository;

@Controller
public class MeetingRoomController {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private MeetingRoomOrderRepository meetingRoomOrderRepository;
    @Autowired
    private MeetingRoomPhotoRepository meetingRoomPhotoRepository; // 假設你有一個用來操作會議廳照片的Repository

    @GetMapping("/conference/order")
    public String orderPage(Model model){
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        model.addAttribute("meetingRooms", meetingRooms);

        return "front-end/conference/conference_order";
    }
    @GetMapping("/conference/calendar/{meetingRoomId}")
    public ResponseEntity<List<MeetingRoomOrder>> calendar(@PathVariable Integer meetingRoomId){

        List<MeetingRoomOrder> orders = meetingRoomOrderRepository.findByMeetingRoomMeetingRoomId(meetingRoomId);
        return ResponseEntity.ok().body(orders);
    }
    @GetMapping("/conference/calendar/json/{meetingRoomId}")
    public ResponseEntity<List<Map<String, Object>>> calendarJson(@PathVariable Integer meetingRoomId) throws JsonProcessingException {
        List<MeetingRoomOrder> orders = meetingRoomOrderRepository.findByMeetingRoomMeetingRoomId(meetingRoomId);
        List<Map<String, Object>> eventData = new ArrayList<>();
        for (MeetingRoomOrder order : orders) {
            Map<String, Object> event = new HashMap<>();
            event.put("start", order.getBookingDate());
            eventData.add(event);
        }
        return ResponseEntity.ok().body(eventData);
    }

   	@GetMapping("/conference")
    public String conference(Model model){
        return "front-end/conference/conference";
    }

    @GetMapping("/conference/venue")
    public String conferenceVenue(Model model){
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        model.addAttribute("meetingRooms", meetingRooms);

        return "front-end/conference/conference_venue";
    }

    @GetMapping("/conference/backend/list")
    public String getAllMeetingRooms(Model model) {
        List<MeetingRoom> meetingRooms = meetingRoomRepository.findAll();
        model.addAttribute("meetingRooms", meetingRooms);
        return "back-end/conference/conference";
    }

    @GetMapping("/conference/backend/add")
    public String showAddForm(Model model) {
        model.addAttribute("meetingRoom", new MeetingRoom());
        return "back-end/conference/add";
    }

    @PostMapping("/conference/backend/add")
    public String submitAddForm(@Validated @ModelAttribute("meetingRoom") MeetingRoom meetingRoom,
                                BindingResult bindingResult,
                                @RequestParam("file") MultipartFile file
                                ) throws IOException, SQLException {
        if (bindingResult.hasErrors()){
            return "back-end/conference/add";
        }

        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        MeetingRoomPhoto meetingRoomPhoto = new MeetingRoomPhoto();
        meetingRoomPhoto.setMeetingRoom(savedMeetingRoom);
        meetingRoomPhoto.setPhotoPath(file.getOriginalFilename());
        meetingRoomPhoto.setMeetingRoomImage(new SerialBlob(file.getBytes()));
        meetingRoomPhotoRepository.save(meetingRoomPhoto);

        return "redirect:/conference/backend/list";
    }

    @GetMapping("/conference/backend/detail/{meetingRoomId}")
    public String showMeetingRoomDetail(@PathVariable Integer meetingRoomId, Model model) {
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Meeting Room ID:" + meetingRoomId));
        model.addAttribute("meetingRoom", meetingRoom);
        return "back-end/conference/detail";
    }


    @GetMapping("/conference/backend/edit/{meetingRoomId}")
    public String showEditForm(@PathVariable("meetingRoomId") Integer meetingRoomId, Model model) {
        if (meetingRoomRepository.existsById(meetingRoomId)) {
            MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Meeting Room ID:" + meetingRoomId));
            model.addAttribute("meetingRoom", meetingRoom);
            return "/back-end/conference/edit";
        } else if (meetingRoomPhotoRepository.existsById(meetingRoomId)) {
            MeetingRoomPhoto existingImage = meetingRoomPhotoRepository.findById(meetingRoomId).orElse(null);
            if (existingImage == null) {
                return "redirect:/conference/backend/list";
            }
            model.addAttribute("image", existingImage);
            return "/back-end/conference/edit";
        } else {
            throw new IllegalArgumentException("Invalid ID:" + meetingRoomId);
        }
    }

    @PostMapping("/conference/backend/update/{meetingRoomId}")
    public String updateState(@PathVariable("meetingRoomId") Integer meetingRoomId,
                              @ModelAttribute MeetingRoom updatedMeetingRoom){
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId).orElse(null);
        meetingRoom.setMeetingRoomSaleState(updatedMeetingRoom.getMeetingRoomSaleState());
        meetingRoomRepository.save(meetingRoom);
        return "redirect:/conference/backend/list";
    }

    @PostMapping("/conference/backend/edit/{meetingRoomId}")
    public String submitEditForm(@PathVariable("meetingRoomId") Integer meetingRoomId,
                                 @ModelAttribute MeetingRoom updatedMeetingRoom,
                                 @RequestParam("file") MultipartFile file) throws IOException, SQLException {
        meetingRoomRepository.findById(meetingRoomId).ifPresent(meetingRoom -> {
            meetingRoom.setMeetingRoomName(updatedMeetingRoom.getMeetingRoomName());
            meetingRoom.setMeetingRoomEname(updatedMeetingRoom.getMeetingRoomEname());
            meetingRoom.setMeetingRoomContent(updatedMeetingRoom.getMeetingRoomContent());
            meetingRoom.setMeetingRoomPrice(updatedMeetingRoom.getMeetingRoomPrice());
            meetingRoom.setMeetingRoomChair(updatedMeetingRoom.getMeetingRoomChair());
            meetingRoom.setMeetingRoomSquareMeter(updatedMeetingRoom.getMeetingRoomSquareMeter());
            meetingRoom.setMeetingRoomCapacity(updatedMeetingRoom.getMeetingRoomCapacity());
            try {
                if (!file.isEmpty()) {
                    MeetingRoomPhoto existingPhoto = meetingRoomPhotoRepository.findByMeetingRoomMeetingRoomId(meetingRoomId);
                    if (existingPhoto != null) {
                        existingPhoto.setPhotoPath(file.getOriginalFilename());
                        existingPhoto.setMeetingRoomImage(new SerialBlob(file.getBytes()));
                        meetingRoomPhotoRepository.save(existingPhoto);
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            meetingRoomRepository.save(meetingRoom);
        });
        return "redirect:/conference/backend/list";
    }

    @GetMapping("/conference/backend/delete/{meetingRoomId}")
    public String deleteMeetingRoom(@PathVariable("meetingRoomId") Integer meetingRoomId) {
        meetingRoomRepository.deleteById(meetingRoomId);
        return "redirect:/conference/backend/list";
    }
}

