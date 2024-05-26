package com.joyfulresort.yu.roomtypephoto.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;
import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhotoRepository;

@Service("roomTypePhotoService")
public class RoomTypePhotoService {

	@Autowired
	RoomTypePhotoRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public RoomTypePhotoService(RoomTypePhotoRepository repository) {
		this.repository = repository;
	}
	
	public RoomTypePhoto addRoomTypePhoto(RoomTypePhoto roomTypePhoto) {
		repository.save(roomTypePhoto);
		return roomTypePhoto;
	}
	

	public RoomTypePhoto updateRoomTypePhoto(RoomTypePhoto roomTypePhoto) {
		repository.save(roomTypePhoto);
		return roomTypePhoto;
	}
	
	public List<RoomTypePhoto> getAll() {
		return repository.findAll();
	}
	
	public RoomTypePhoto getOneRoomTypePhoto(Integer roomTypePhotoId) {
		Optional<RoomTypePhoto> optional = repository.findById(roomTypePhotoId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	
}
