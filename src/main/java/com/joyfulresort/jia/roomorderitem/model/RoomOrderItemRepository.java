// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.joyfulresort.jia.roomorderitem.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.jia.roomorder.model.RoomOrder;

import java.util.List;


public interface RoomOrderItemRepository extends JpaRepository<RoomOrderItem, Integer> {

	 @Transactional
	    @Query(value = "select roi from RoomOrderItem roi where roi.roomOrder = ?1")
	    List<RoomOrderItem> findByRoomOrder(RoomOrder roomOrder);
	
	
	

}