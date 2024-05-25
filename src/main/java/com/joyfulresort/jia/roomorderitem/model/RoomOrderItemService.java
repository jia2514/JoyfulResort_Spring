package com.joyfulresort.jia.roomorderitem.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.naming.java.javaURLContextFactory;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderRepository;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRepository;
import com.joyfulresort.yu.room.model.Room;
import com.joyfulresort.yu.room.model.RoomRepository;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomOrder;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomSchedule;

@Service("roomOrderItemService")
public class RoomOrderItemService {

	@Autowired
	RoomOrderItemRepository repository;
	
	@Autowired
	RoomScheduleRepository rsRepository;
	
	@Autowired
	RoomOrderService roSvc;
	

	@Autowired
	private SessionFactory sessionFactory;

	public List<RoomOrderItem> getAll() {
		return repository.findAll();
	}

	public List<RoomOrderItem> getByRoomOrder(RoomOrder roomOrder){
		return repository.findByRoomOrder(roomOrder);
	}
	
	public RoomOrderItem checkout(Integer roomOrderItemId) {
		RoomOrderItem roomOrderItem = getOneRoomOrderItem(roomOrderItemId);
		Room room = roomOrderItem.getRoom();
		room.setRoomState((byte)2);

        roomOrderItem.setRoomOrderItemState((byte)2);
		RoomOrder roomOrder = roomOrderItem.getRoomOrder();
		Set<RoomOrderItem> set = roomOrder.getRoomOrderItems();
		int count=0;
		for(RoomOrderItem roi : set) {
			if(roi.getRoomOrderItemState()==2 || roi.getRoomOrderItemState()==3 || roi.getRoomOrderItemState()==4) {
				count++;
			}
		}
		if(count==set.size()) {
			roomOrder.setRoomOrderState((byte)2);
			roomOrder.setCompleteDateTime(new Timestamp(System.currentTimeMillis()));
		}

		roomOrderItem.setRoom(room);
		roomOrderItem.setRoomOrder(roomOrder);

		return repository.saveAndFlush(roomOrderItem); 
        	
				 
	}
	

	public void addRoomOrderItem(RoomOrderItem roomOrderItem) {
		repository.save(roomOrderItem);
	}

	public RoomOrderItem updateRoomOrderItem(RoomOrderItem roomOrderItem) {
		repository.save(roomOrderItem);
		return getOneRoomOrderItem(roomOrderItem.getRoomOrderItemId());
	}

	public RoomOrderItem getOneRoomOrderItem(Integer roomOrderItemId) {
		Optional<RoomOrderItem> optional = repository.findById(roomOrderItemId);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	@Transactional
	public RoomOrderItem cancelRoomOrderItem(Integer roomOrderItemId) {
		RoomOrderItem roomOrderItem = getOneRoomOrderItem(roomOrderItemId);
		roomOrderItem.setRoomOrderItemState((byte)4);
		RoomOrder roomOrder = roomOrderItem.getRoomOrder();
		Set<RoomOrderItem> set = roomOrder.getRoomOrderItems();
		int count=0;
		for(RoomOrderItem roi : set) {
			if(roi.getRoomOrderItemState()==4) {
				count++;
			}
		}

		if(count==set.size()) {
			roSvc.cancelRoomOrder(roomOrder.getRoomOrderId());
			return getOneRoomOrderItem(roomOrderItemId);
		}
		
		
        rsRepository.deleteAll(roomOrderItem.getRoomSchedules());
        roomOrderItem.setRoomSchedules(null);
        roomOrderItem.setRoomOrder(roomOrder);
        
        return repository.saveAndFlush(roomOrderItem);
       
	}

}