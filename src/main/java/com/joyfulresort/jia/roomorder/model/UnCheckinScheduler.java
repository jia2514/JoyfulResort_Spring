package com.joyfulresort.jia.roomorder.model;

import java.sql.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	 @Transactional
	    public void cancelUnCheckinOrders() {
		 	System.out.println("執行排程器");
		 	
		 	List<RoomOrder> roomOrders =roomOrderSvc.getAll();
		 	System.out.println(roomOrders);
	        for(RoomOrder roomOrder:roomOrders) {
	        	System.out.println(roomOrderItemSvc.getByRoomOrder(roomOrder));
	        	for(RoomOrderItem roomOrderItem:roomOrderItemSvc.getByRoomOrder(roomOrder)) {
	        		System.out.println(roomOrderItem);
	        		String date = roomOrder.getCheckInDate().toString();
	        		
		        	Byte roomOrderItemState = roomOrderItem.getRoomOrderItemState();
		        	String todayDate = new Date(System.currentTimeMillis()).toString();
		        	System.out.println(date);
		        	System.out.println(todayDate);
		        	System.out.println(date.equals(todayDate));
	        		System.out.println(roomOrderItemState);
	        		
		        	if(date.equals(todayDate) && roomOrderItemState==0) {
		        		System.out.println(roomOrderItem.getRoomOrderItemId());
		        		roomOrderItemSvc.cancelRoomOrderItem(roomOrderItem.getRoomOrderItemId(),(byte)3);
		        	}
	        	}
	        	
	        	
	        }
	    }
}
