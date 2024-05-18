package com.joyfulresort.ool.meetingroomphoto;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.ool.meetingroom.MeetingRoom;
import com.joyfulresort.ool.meetingroom.MeetingRoomRepository;

@Controller
public class MeetingRoomPhotoController {

    @Autowired
    private MeetingRoomPhotoRepository meetingRoomPhotoRepository;
    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @GetMapping("/upload/{meetingRoomId}")
    public String showUploadForm(@PathVariable Integer meetingRoomId, Model model) {
        model.addAttribute("meetingRoomId", meetingRoomId);
        return "back-end/conference/add";
    }

    @PostMapping("/upload/{meetingRoomId}")
    public String uploadPhoto(@PathVariable Integer meetingRoomId, @RequestParam("file") MultipartFile file) throws IOException, SQLException {
        MeetingRoomPhoto meetingRoomPhoto = new MeetingRoomPhoto();
        meetingRoomPhoto.setMeetingRoom(meetingRoomRepository.findById(meetingRoomId).orElse(null));
        meetingRoomPhoto.setPhotoPath(file.getOriginalFilename());
        meetingRoomPhoto.setMeetingRoomImage(new SerialBlob(file.getBytes()));
        meetingRoomPhotoRepository.save(meetingRoomPhoto);
        return "redirect:/conference/backend/list";
    }

    @GetMapping("/photos/{meetingRoomId}")
    public String showMeetingRoomPhotos(@PathVariable Integer meetingRoomId, Model model) {
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Meeting Room ID:" + meetingRoomId));
        List<MeetingRoomPhoto> photos = meetingRoom.getPhotos();
        model.addAttribute("meetingRoom", meetingRoom);
        model.addAttribute("photos", photos);
        return "back-end/conference/conference";
    }


    @GetMapping("/image/{meetingRoomPhotoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer meetingRoomPhotoId) throws SQLException {
        MeetingRoomPhoto image = meetingRoomPhotoRepository.findById(meetingRoomPhotoId).orElse(null);
        if(image != null){
            Blob blob = image.getMeetingRoomImage();
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(blobAsBytes);
        }else {
            return ResponseEntity.notFound().build();
        }
    }




}
