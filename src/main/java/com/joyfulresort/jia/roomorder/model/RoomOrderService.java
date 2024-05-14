package com.joyfulresort.jia.roomorder.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.naming.java.javaURLContextFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.he.member.model.MemberRepository;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.jia.roomschedule.model.RoomScheduleRepository;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomOrder;

@Service("roomOrderService")
public class RoomOrderService {

	@Autowired
	RoomOrderRepository repository;
	
	@Autowired
	RoomScheduleRepository rsRepository;
	
	@Autowired
    private SessionFactory sessionFactory;
		
	
	public List<RoomOrder> getAll() {
		return repository.findAll();
	}
	
	public List<RoomOrder> getTodayCheckIn(){
		return repository.findByTodayCheckIn();
	}

	public List<RoomOrder> getTodayCheckOut(){
		return repository.findByTodayCheckOut();
	}
	
	public List<RoomOrder> getAll(Map<String, String[]> map) {
		Map<String, String> query = new HashMap<>();
		// Map.Entry即代表一組key-value
		Set<Map.Entry<String, String[]>> entry = map.entrySet();

		for (Map.Entry<String, String[]> row : entry) {
			String key = row.getKey();
			// 因為請求參數裡包含了action，做個去除動作
			if ("action".equals(key)) {
				continue;
			}
			// 若是value為空即代表沒有查詢條件，做個去除動作
			String value = row.getValue()[0]; // getValue拿到一個String陣列, 接著[0]取得第一個元素檢查
			if (value == null || value.isEmpty()) {
				continue;
			}
			query.put(key, value);
		}
		System.out.println("60+"+query);
		HibernateUtil_CompositeQuery_RoomOrder composite = new HibernateUtil_CompositeQuery_RoomOrder();
		System.out.println("61+"+composite.getAllC(query,sessionFactory.openSession()));
		return composite.getAllC(query,sessionFactory.openSession());
	}
	
	@Transactional
	public RoomOrder cancelRoomOrder(Integer roomOrderId) {
		RoomOrder roomOrder = getOneRoomOrder(roomOrderId);
		
		long miliseconds = System.currentTimeMillis();
		long checkInMiliseconds = roomOrder.getCheckInDate().getTime();
		long differenceInTime = checkInMiliseconds - miliseconds;
        int differenceInDays = (int)(differenceInTime / (1000 * 3600 * 24));

        if (differenceInDays <= 3) {
        	roomOrder.setRoomOrderState((byte)0);
        	roomOrder.setRefundState((byte)0);
        	roomOrder.setCompleteDateTime(new Timestamp(miliseconds));
        	for(RoomOrderItem roomOrderItem :roomOrder.getRoomOrderItems()) {
        		rsRepository.deleteAll(roomOrderItem.getRoomSchedules());
        		roomOrderItem.setRoomSchedules(null);
        		roomOrderItem.setRoomOrderItemState((byte)4);
        		roomOrderItem.setSpecialREQ("距離入住日期不足三天取消, 無退款");
        	}
            return repository.saveAndFlush(roomOrder);
        } else {
        	roomOrder.setRoomOrderState((byte)3);
        	roomOrder.setRefundState((byte)1);
        	return repository.saveAndFlush(roomOrder);
        }
	}
	
	
	@Transactional
	public RoomOrder refundRoomOrder(Integer roomOrderId) {
		RoomOrder roomOrder = getOneRoomOrder(roomOrderId);
		long miliseconds = System.currentTimeMillis();
		roomOrder.setRoomOrderState((byte)0);
    	roomOrder.setRefundState((byte)2);
    	roomOrder.setCompleteDateTime(new Timestamp(miliseconds));
    	for(RoomOrderItem roomOrderItem :roomOrder.getRoomOrderItems()) {
    		rsRepository.deleteAll(roomOrderItem.getRoomSchedules());
    		roomOrderItem.setRoomSchedules(null);
    		roomOrderItem.setRoomOrderItemState((byte)4);
    		roomOrderItem.setSpecialREQ("已退款");
    	}
		return repository.saveAndFlush(roomOrder);
	}
	
	public void addRoomOrder(RoomOrder roomOrder) {
		
		RoomOrder ro=repository.saveAndFlush(roomOrder);
		System.out.println("ro+"+ro);
	}
	
	
	
	
	
	
	
	

	public void updateRoomOrder(RoomOrder roomOrder) {
		repository.save(roomOrder);
	}

	public RoomOrder getOneRoomOrder(Integer roomOrderId) {
		Optional<RoomOrder> optional = repository.findById(roomOrderId);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	

	
	

	
	public RoomOrder completeRoomOrder(Integer roomOrderId) {
		RoomOrder roomOrder = getOneRoomOrder(roomOrderId);
		roomOrder.setRoomOrderState((byte)2);
    	roomOrder.setRefundState((byte)0);
		return repository.saveAndFlush(roomOrder);
	}

	public Set<RoomOrderItem> getRoomOrderItemsByRoomOrderId(Integer roomOrderId) {
		RoomOrder roomOrder = getOneRoomOrder(roomOrderId);
		return roomOrder.getRoomOrderItems();
	}

	
	
	
	
	
	
}