package com.joyfulresort.jia.roomtypephoto;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhotoService;

@Controller
@RequestMapping("/roomtypephotojia")
public class RoomTypePhotoControllerJia {
		
	@Autowired
	RoomTypePhotoServiceJia roomTypePhotoSvc;
	
	@Autowired
	RoomTypeService roomTypeSvc;
	
	
	
	@PostMapping("getPhotosByRoomTypeId")
	public ResponseEntity getByRoomTypeId(@RequestParam("roomTypeId") String roomTypeId) {
		Integer rtId = Integer.valueOf(roomTypeId);
		List<Integer> roomTypePhotoIdList = roomTypePhotoSvc.findByRoomTypeId(rtId);
		return ResponseEntity.ok(roomTypePhotoIdList);
	}
	
}
