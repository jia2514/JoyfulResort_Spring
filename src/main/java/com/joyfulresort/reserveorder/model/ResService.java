package com.joyfulresort.reserveorder.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("resService")
public class ResService {

	@Autowired
	private ResRepository repository;

	public void addRes(ResVO resVO) {
		repository.save(resVO);
	}

	public void updateRes(ResVO resVO) {
		repository.save(resVO);
	}

	public void deleteRes(Integer resId) {
		if (repository.existsById(resId)) {
			repository.deleteById(resId);
		}
	}

	public ResVO getOneRes(Integer resId) {
		Optional<ResVO> optional = repository.findById(resId);
		return optional.orElse(null);
	}

	public List<ResVO> getAllRes() {
//        return repository.findAll(); 預設方法
		return repository.findAllRes();
	}

//	人數判斷	

//	public Integer countNumber(LocalDate date) {
//		Integer totalNumber = repository.countNumber(date);
//		return totalNumber;
//	}

	public Integer countNumber101(LocalDate bookingDate) {
		if (bookingDate != null) {

//			System.out.println(bookingDate);
			return repository.countNumber101(bookingDate);
		} else {
			return -11;
		}

	}

	public Integer countNumber102(LocalDate bookingDate) {
		if (bookingDate != null) {
			return repository.countNumber102(bookingDate);

		} else {
			return -11;
		}
	}

//    ------------------------------------複合查詢

	public List<ResVO> findByDates(LocalDate reserveOrderDate, LocalDate bookingDate) {

		if (reserveOrderDate != null && bookingDate != null) {
			// 兩個日期都存在，進行複合查詢

			return repository.findByDates(reserveOrderDate, bookingDate);
		} else if (reserveOrderDate != null) {

			// 只有 reserveOrderDate 存在，單獨查詢 reserveOrderDate
			return repository.findByReserveOrderDate(reserveOrderDate);
		} else if (bookingDate != null) {

			// 只有 bookingDate 存在，單獨查詢 bookingDate
			return repository.findByBookingDate(bookingDate);

		} else {
			return repository.findAllRes();
		}

	}

}
