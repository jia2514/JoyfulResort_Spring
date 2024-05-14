package com.joyfulresort.yu.roomtype.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeRepository;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomType;

@Service("roomTypeService")
public class RoomTypeService {
	
	@Autowired
	RoomTypeRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public RoomTypeService(RoomTypeRepository repository) {
		this.repository = repository;
	}

	public RoomType addRoomType(RoomType roomType) {
		repository.save(roomType);
		return roomType;
	}
	
	public RoomType getOnebyId(Integer roomTypeId) {
		Optional<RoomType> optional = repository.findById(roomTypeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public RoomType updateRoomType(RoomType roomType) {
		repository.save(roomType);
		return roomType;
	}

//	public void deleteRoomType(Integer roomType) {
//		if (repository.existsById(roomType))
//			repository.deleteByEmpno(roomType);
//		    repository.deleteById(roomType);
//	}

	public List<RoomType> getAll() {
		return repository.findAll();
	}

	public RoomType getOneRoomType(Integer roomTypeId) {
		Optional<RoomType> optional = repository.findById(roomTypeId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
		
//	public List<RoomType> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_RoomType.getAllC(map,sessionFactory.openSession()); 
//		//**重要!!關鍵行
//	}
	}
}
