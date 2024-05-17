// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.joyfulresort.jia.roomorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.yu.room.model.Room;

public interface RoomOrderRepository extends JpaRepository<RoomOrder, Integer> {

	@Transactional
	@Query(value = "select * from room_order where check_in_date =CURDATE()", nativeQuery = true)
	List<RoomOrder> findByTodayCheckIn();
	
	@Transactional
	@Query(value = "select * from room_order where check_out_date =CURDATE()", nativeQuery = true)
	List<RoomOrder> findByTodayCheckOut();
	
	
	//---使用會員編號 查詢會員個人訂單---
	@Query(nativeQuery = true, value = "SELECT * FROM room_order WHERE member_id=?1")
	List<RoomOrder> findRoomOrderByMemberId(Integer memberId);
		
	//--- 尋找房型編號 ---
	@Query(nativeQuery = true, value = "SELECT DISTINCT room_type_id FROM room_order_item roi JOIN room_order ro ON ro.room_order_id =roi.room_order_id WHERE member_id=?1")
	List<Integer> findRoomrTypeByMemberId(Integer memberId);
				
		
		

}