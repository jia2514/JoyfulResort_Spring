package com.joyfulresort.jia.roomschedule.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service("roomScheduleRedisService")
public class RoomScheduleRedisService {

	public void saveOrUpdateRedisRoomSchedule(List<Object[]> list) {
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.select(5);
		
		try {
            
            for (Object[] obj : list) {
                String date = obj[0].toString();
                String roomType = obj[1].toString();
                String checkInCount = obj[2].toString();
                String availableRooms = obj[3].toString();
                
                String key = date + ":" + roomType;
                jedis.hset(key, "checkInCount", checkInCount);
                jedis.hset(key, "availableRooms", availableRooms);
            }

            System.out.println("Data stored successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
		
		
	}
	
	
	public class RetrieveHotelData {
	    public static void main(String[] args) {
	        Jedis jedis = new Jedis("localhost", 6379);

	        try {
	            String roomType = "1";
	            String startDate = "2024-05-25";
	            String endDate = "2024-06-01";

	            int minAvailableRooms = getMinAvailableRooms(jedis, roomType, startDate, endDate);
	            if (minAvailableRooms == Integer.MAX_VALUE) {
	                System.out.println("No data found for the specified range and room type.");
	            } else {
	                System.out.println("Minimum available rooms for room type " + roomType + " from " + startDate + " to " + endDate + ": " + minAvailableRooms);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            jedis.close();
	        }
	    }

	    public static int getMinAvailableRooms(Jedis jedis, String roomType, String startDate, String endDate) throws Exception {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date start = sdf.parse(startDate);
	        Date end = sdf.parse(endDate);

	        long diffInMillies = Math.abs(end.getTime() - start.getTime());
	        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	        int minAvailableRooms = Integer.MAX_VALUE;

	        for (int i = 0; i <= diff; i++) {
	            Date currentDate = new Date(start.getTime() + TimeUnit.DAYS.toMillis(i));
	            String dateStr = sdf.format(currentDate);
	            String key = dateStr + ":" + roomType;
	            
	            String availableRoomsStr = jedis.hget(key, "availableRooms");
	            if (availableRoomsStr != null) {
	                int availableRooms = Integer.parseInt(availableRoomsStr);
	                minAvailableRooms = Math.min(minAvailableRooms, availableRooms);
	            }
	        }
	        return minAvailableRooms;
	    }
	
	}
}
