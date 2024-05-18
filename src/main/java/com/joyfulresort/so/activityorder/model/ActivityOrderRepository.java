package com.joyfulresort.so.activityorder.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ActivityOrderRepository extends JpaRepository<ActivityOrderVO, Integer> {
	
	@Transactional
	@Query(value = "SELECT * from activity_order where activity_session_id =?1", nativeQuery = true)
	List<ActivityOrderVO> findBySession(Integer activitySessionID);
	
	
	//透過會員ID查找活動訂單
	@Query(nativeQuery = true ,value = "SELECT * FROM activity_order WHERE member_id =?1")
	List<ActivityOrderVO> findActivityOrderByMemberId(Integer memberID);

}
