package com.joyfulresort.jia.roomorder.model;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItemService;

@Component
@EnableScheduling
public class UnCheckinScheduler {

	@Autowired
	RoomOrderItemService roomOrderItemSvc;
	@Autowired
	RoomOrderService roomOrderSvc;
	
	 @Scheduled(cron = "0 0 18 * * *") // 每天 18:00 
	    public void cancelUnCheckinOrders() {
		 	System.out.println("執行排程器");
//		 	List<RoomOrder> roomOrders =roomOrderSvc.getAll();
//	        
//	        for(RoomOrder roomOrder:roomOrders) {
//	        	Set<RoomOrderItem> roomOrderItems = roomOrder.getRoomOrderItems();
//	        	for(RoomOrderItem roomOrderItem:roomOrderItems) {
//	        		Date date = roomOrder.getCheckInDate();
//		        	Byte roomOrderItemState = roomOrderItem.getRoomOrderItemState();
//		        	if(date == new Date(System.currentTimeMillis()) && roomOrderItemState==0) {
//		        		Hibernate.initialize(roomOrderItem.getRoomOrder().getRoomOrderItems());
//		                roomOrderItemSvc.cancelRoomOrderItem(roomOrderItem.getRoomOrderItemId());
//		        	}
//	        	}
//	        	
//	        	
//	        }
	    }
}
