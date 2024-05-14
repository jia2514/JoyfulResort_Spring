package com.joyfulresort.jia.roomschedule.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.sql.Date;
import java.sql.Timestamp;

import org.apache.naming.java.javaURLContextFactory;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomOrder;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomSchedule;

@Service("roomScheduleService")
public class RoomScheduleService {

	@Autowired
	RoomScheduleRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	public List<RoomSchedule> getAll() {
		return repository.findAll();
	}

	public String getALLIn2Month() {
		HibernateUtil_CompositeQuery_RoomSchedule composite = new HibernateUtil_CompositeQuery_RoomSchedule();
		List<Object[]> list = composite.getAllByCurDate(sessionFactory.openSession());
		System.out.println("37+"+list);
		String jsonStr = new JSONArray(list).toString();

		return jsonStr;

	}

	public String getByCompositeQuery(Map<String, String[]> map) {
		Map<String, String> query = new HashMap<>();
		// Map.Entry即代表一組key-value

		Set<Map.Entry<String, String[]>> entry = map.entrySet();

		for (Map.Entry<String, String[]> row : entry) {
			String key = row.getKey();
			// 因為請求參數裡包含了action，做個去除動作
			if ("action".equals(key)) {
				continue;
			}
			// 若是value為空即代表沒有查詢條件，做個去除動作
			String value = row.getValue()[0]; // getValue拿到一個String陣列, 接著[0]取得第一個元素檢查
			if (value == null || value.isEmpty()) {
				continue;
			}

			query.put(key, value);
		}

		if (!query.containsKey("startquerydate") && query.containsKey("endquerydate")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(Date.valueOf(query.get("endquerydate")));
			calendar.add(Calendar.MONTH, -2);
			Date endDate2month = new Date(calendar.getTimeInMillis());
			query.put("startquerydate", endDate2month.toString());

		}
		HibernateUtil_CompositeQuery_RoomSchedule composite = new HibernateUtil_CompositeQuery_RoomSchedule();
		List<Object[]> list = null;

		if (query.containsKey("roomTypeId") && !query.containsKey("startquerydate")
				&& !query.containsKey("endquerydate")) {
			list = composite.getOneByCurDate(query, sessionFactory.openSession());
		} else {

			list = composite.getByCompositeQuery(query, sessionFactory.openSession());
		}
		
		String jsonStr = new JSONArray(list).toString();
		return jsonStr;
	}

	public void addRoomSchedule(RoomSchedule roomSchedule) {
		repository.save(roomSchedule);
	}

	public void updateRoomSchedule(RoomSchedule roomSchedule) {
		repository.save(roomSchedule);
	}

	public RoomSchedule getOneRoomSchedule(Integer roomScheduleId) {
		Optional<RoomSchedule> optional = repository.findById(roomScheduleId);
//		return optional.get();
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

}