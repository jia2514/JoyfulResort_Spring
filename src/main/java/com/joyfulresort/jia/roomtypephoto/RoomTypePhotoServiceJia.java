package com.joyfulresort.jia.roomtypephoto;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhotoRepository;

@Service("roomTypePhotoServiceJia")
public class RoomTypePhotoServiceJia {

	@Autowired
	RoomTypePhotoRepositoryJia repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	
	public List<Integer> findByRoomTypeId(Integer roomTypeId) {
		
		return repository.findByRoomTypeId(roomTypeId);
	}
}
