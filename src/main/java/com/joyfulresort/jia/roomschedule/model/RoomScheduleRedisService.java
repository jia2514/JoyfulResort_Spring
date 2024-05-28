package com.joyfulresort.jia.roomschedule.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import jedis.connectionpool.JedisUtil;
import oracle.sql.DATE;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("roomScheduleRedisService")
public class RoomScheduleRedisService {

	public void saveOrUpdateRedisRoomSchedule(List<Object[]> list) {
		JedisPool pool = JedisUtil.getJedisPool();

		try (Jedis jedis = pool.getResource()) {
			jedis.select(5);

			for (Object[] obj : list) {
				String roomTypeId = obj[0].toString();
				String date = obj[1].toString();
				String checkInCount = obj[2].toString();
				String availableRooms = obj[3].toString();

				String key = roomTypeId + ":" + date;
				jedis.hset(key, "checkInCount", checkInCount);
				jedis.hset(key, "availableRooms", availableRooms);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addOrDeleteRedisRoomSchedule(Date date, Integer roomTypeId, int checkInCountChange,
			int availableRoomsChange) {
		JedisPool pool = JedisUtil.getJedisPool();

		try (Jedis jedis = pool.getResource()) {
			jedis.select(5);

			String key = roomTypeId.toString() + ":" + date.toString();
			if(jedis.hget(key, "checkInCount")!=null && jedis.hget(key, "availableRooms")!=null) {
				String checkInCount = jedis.hget(key, "checkInCount");
				String availableRooms = jedis.hget(key, "availableRooms");
				
				Integer newCheckInCount = Integer.valueOf(checkInCount) + checkInCountChange;
				Integer newAvailableRooms = Integer.valueOf(availableRooms) + availableRoomsChange;
//				System.out.println(key + "checkInCount" + checkInCount + "newCheckInCount" + newCheckInCount
//						+ "availableRooms" + availableRooms + "newAvailableRooms" + newAvailableRooms);
				jedis.hset(key, "checkInCount", newCheckInCount.toString());
				jedis.hset(key, "availableRooms", newAvailableRooms.toString());
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getMinAvailableRooms(String roomTypeId, String checkInDate, String checkOutDate) throws Exception {
		
		List<String> dateList = getDateRange(checkInDate, checkOutDate);
		int minAvailableRooms = Integer.MAX_VALUE;

		JedisPool pool = JedisUtil.getJedisPool();

		try (Jedis jedis = pool.getResource()) {
			jedis.select(5);

			for (String date : dateList) {
				
				String key = roomTypeId + ":" + date;
				String availableRoomsStr = jedis.hget(key, "availableRooms");
//				System.out.println("availableRoomsStr+"+availableRoomsStr);
				if (availableRoomsStr != null) {
					int availableRooms = Integer.parseInt(availableRoomsStr);
					minAvailableRooms = Math.min(minAvailableRooms, availableRooms);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return minAvailableRooms;

	}

	public static List<String> getDateRange(String checkInDate, String checkOutDate) throws Exception {
		List<String> dateList = new ArrayList<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		// 使用 Calendar 將 util.Date 轉換為日期
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(sdf.parse(checkInDate));
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(sdf.parse(checkOutDate));

		// 將日期範圍內的每個日期添加到 List，但不包括 checkOutDate
		while (startDate.before(endDate)) {
			String dateStr = sdf.format(startDate.getTime());
			dateList.add(dateStr);
			startDate.add(Calendar.DATE, 1);
		}

		return dateList;
	}

}
