// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.joyfulresort.jia.roomorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoomOrderRepository extends JpaRepository<RoomOrder, Integer> {

	@Transactional
	@Query(value = "select * from room_order where check_in_date =CURDATE()", nativeQuery = true)
	List<RoomOrder> findByTodayCheckIn();
	
	@Transactional
	@Query(value = "select * from room_order where check_out_date =CURDATE()", nativeQuery = true)
	List<RoomOrder> findByTodayCheckOut();

}