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

//	------------------------------------兩種時段的人數總數計算	

	public Integer countNumber101(LocalDate bookingDate) {
		if (bookingDate != null) {
			return repository.countNumber101(bookingDate);
		} else {
			return 1;
		}

	}

	public Integer countNumber102(LocalDate bookingDate) {
		if (bookingDate != null) {
			return repository.countNumber102(bookingDate);

		} else {
			return 1;
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
// -------------------------------------------起始&結束日查詢	

	public List<ResVO> findByDateBetween(LocalDate TimeStart, LocalDate TimeEnd) {
		if (TimeStart != null && TimeEnd != null) {
			return repository.findByDateBetween(TimeStart, TimeEnd);
		} else if (TimeStart != null) {
			return repository.findByDateStart(TimeStart);
		} else if (TimeEnd != null) {
			return repository.findByDateEnd(TimeEnd);
		} else {
			return repository.findAllRes();
		}
	}

}
