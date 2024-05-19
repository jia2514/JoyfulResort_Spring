package com.joyfulresort.reserveorder.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ResRepository extends JpaRepository<ResVO, Integer> {

//	@Transactional
	@Modifying
	@Query(value = "SELECT * from reserve_order ORDER BY reserve_order_id DESC", nativeQuery = true)
	List<ResVO> findAllRes();

	@Query(value = "SELECT * FROM reserve_order WHERE reserve_order_date LIKE ?1 AND booking_date LIKE ?2", nativeQuery = true)
	List<ResVO> findByDates(LocalDate reserveOrderDate, LocalDate bookingDate);

	@Query(value = "SELECT * FROM reserve_order WHERE reserve_order_date LIKE %?1%", nativeQuery = true)
	List<ResVO> findByReserveOrderDate(LocalDate reserveOrderDate);

	@Query(value = "SELECT * FROM reserve_order WHERE booking_date LIKE %?1%", nativeQuery = true)
	List<ResVO> findByBookingDate(LocalDate bookingDate);

//------------------------------------------
	@Query(value = "SELECT * FROM reserve_order WHERE booking_date BETWEEN ?1 AND ?2 ", nativeQuery = true)
	List<ResVO> findByDateBetween(LocalDate TimeStart, LocalDate TimeEnd);

	@Query(value = "SELECT * FROM reserve_order WHERE booking_date >= ?1  ", nativeQuery = true)
	List<ResVO> findByDateStart(LocalDate TimeStart);
	
	@Query(value = "SELECT * FROM reserve_order WHERE booking_date <= ?1   ", nativeQuery = true)
	List<ResVO> findByDateEnd(LocalDate TimeEnd);
	
//------------------------------------------
	@Query(value = "SELECT SUM(reserve_number) FROM reserve_order  WHERE booking_date like %?1% AND DATE_FORMAT(booking_date, '%H:%i:%s') < '15:00:00' AND reserve_order_state <> 0", nativeQuery = true)
	Integer countNumber101(LocalDate bookingDate);

	@Query(value = "SELECT SUM(reserve_number) FROM reserve_order WHERE booking_date LIKE %?1% AND (DATE_FORMAT(booking_date, '%H:%i:%s') > '16:00:00' and DATE_FORMAT(booking_date, '%H:%i:%s') < '21:00:00')AND reserve_order_state <> 0", nativeQuery = true)
	Integer countNumber102(LocalDate bookingDate);

	// 使用會員編號 查找餐廳訂單
	@Query(nativeQuery = true, value = "SELECT * FROM reserve_order WHERE member_id")
	List<ResVO> findmemberReserveOrderByMemberId(Integer memberID);
}
