package com.joyfulresort.jia.roomorderitem.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderService;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRedisService;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRepository;
import com.joyfulresort.yu.room.model.Room;

@Service("roomOrderItemService")
public class RoomOrderItemService {

	@Autowired
	RoomOrderItemRepository repository;
	
	@Autowired
	RoomScheduleRepository rsRepository;
	
	@Autowired
	RoomOrderService roSvc;
	
	@Autowired
	RoomScheduleRedisService rsRedisSvc;
	

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
			if(roi.getRoomOrderItemState()==2 || roi.getRoomOrderItemState()==3) {
				count++;
			}
		}
		if(count==set.size()) {
			roomOrder.setRoomOrderState((byte)2);
			roomOrder.setCompleteDateTime(new Timestamp(System.currentTimeMillis()));
		}
		Set<RoomSchedule> rsSet = new LinkedHashSet<>();
		for(RoomSchedule roomSchedule :roomOrderItem.getRoomSchedules()) {
			Date date=roomSchedule.getRoomScheduleDate();
			if (date.getTime()> System.currentTimeMillis()) {
				System.out.println(roomSchedule);
				Integer roomTypeId= roomSchedule.getRoomType().getRoomTypeId();
				rsRedisSvc.addOrDeleteRedisRoomSchedule(date, roomTypeId, -1, 1);
				roomOrderItem.getRoomSchedules().remove(roomSchedule);
				rsRepository.delete(roomSchedule);
			}else {
				rsSet.add(roomSchedule);
			}
		}
		
		roomOrderItem.setRoomSchedules(rsSet);
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
		roomOrderItem.setRoomOrderItemState((byte)3);
		RoomOrder roomOrder = roomOrderItem.getRoomOrder();
		Set<RoomOrderItem> set = roomOrder.getRoomOrderItems();
		int count=0;
		for(RoomOrderItem roi : set) {
			if(roi.getRoomOrderItemState()==2 || roi.getRoomOrderItemState()==3) {
				count++;
			}
		}

		if(count==set.size()) {
			roSvc.cancelRoomOrder(roomOrder.getRoomOrderId());
			return getOneRoomOrderItem(roomOrderItemId);
		}
		for(RoomSchedule roomSchedule :roomOrderItem.getRoomSchedules()) {
			Date date=roomSchedule.getRoomScheduleDate();
			Integer roomTypeId= roomSchedule.getRoomType().getRoomTypeId();
			rsRedisSvc.addOrDeleteRedisRoomSchedule(date, roomTypeId, -1, 1);
		}
		
        rsRepository.deleteAll(roomOrderItem.getRoomSchedules());
        roomOrderItem.setRoomSchedules(null);
        roomOrderItem.setRoomOrder(roomOrder);
        
        return repository.saveAndFlush(roomOrderItem);
       
	}
	
	

}