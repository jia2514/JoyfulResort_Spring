// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.joyfulresort.jia.roomschedule.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {

//	@Transactional
//	@Modifying
//	@Query(value = "delete from emp3 where empno =?1", nativeQuery = true)
//	void deleteByEmpno(int empno);

}