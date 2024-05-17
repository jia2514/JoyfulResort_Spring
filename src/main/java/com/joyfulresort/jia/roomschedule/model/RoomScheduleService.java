package com.joyfulresort.jia.roomschedule.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joyfulresort.yu.roomtype.model.RoomType;
import com.joyfulresort.yu.roomtype.model.RoomTypeRepository;
import com.joyfulresort.yu.roomtype.model.RoomTypeService;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_RoomSchedule;

@Service("roomScheduleService")
public class RoomScheduleService {

	@Autowired
	RoomScheduleRepository repository;
	@Autowired
	RoomTypeService rtSvc;

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

	public List<Map<RoomType, Integer>> getByPeopleAmount(Map<String, String[]> map) {
		Map<String, String> query = new HashMap<>();
		Set<Map.Entry<String, String[]>> entry = map.entrySet();
		int peopleAmount = 0;
		for (Map.Entry<String, String[]> row : entry) {
			String key = row.getKey();
			if ("action".equals(key)) {
				continue;
			}
			if ("peopleAmount".equals(key)) {
				peopleAmount = Integer.valueOf(row.getValue()[0]);
				continue;
			}
			String value = row.getValue()[0];
			if (value == null || value.isEmpty()) {
				continue;
			}
			
			query.put(key, value);
		}
		
		
		if (query.containsKey("endquerydate")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(Date.valueOf(query.get("endquerydate")));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date endDate1Day = new Date(calendar.getTimeInMillis());
			query.put("endquerydate", endDate1Day.toString());
		}
		System.out.println("query+"+query);
		
		HibernateUtil_CompositeQuery_RoomSchedule composite = new HibernateUtil_CompositeQuery_RoomSchedule();
		List<Object[]> list = null;
		
//		if(peopleAmount == 1 || peopleAmount ==2) {
//			query.put("roomTypeId","1");
//		}
		 
		list = composite.getByCompositeQuery(query, sessionFactory.openSession());
		
		Map<Integer, Integer> minEmptyRooms = new HashMap<>();
		for (Object[] item : list) {
            int roomType = (int) item[0]; // 房型
            int emptyRooms = (int) item[3]; // 空房数
            if (!minEmptyRooms.containsKey(roomType) || emptyRooms < minEmptyRooms.get(roomType)) {
                minEmptyRooms.put(roomType, emptyRooms);
            }
        }
		System.out.println("minEmptyRooms+"+minEmptyRooms);
		
		int type1Empty =0;
		int type2Empty =0;
		int type3Empty =0;
		for (Map.Entry<Integer, Integer> entryEmpty : minEmptyRooms.entrySet()) {
		    int roomType = entryEmpty.getKey();
		    int minEmpty = entryEmpty.getValue();
		    switch (roomType) {
		        case 1:
		            type1Empty += minEmpty;
		            break;
		        case 2:
		            type2Empty += minEmpty;
		            break;
		        case 3:
		            type3Empty += minEmpty;
		            break;
		        default:
		            break;
		    }
		}
		
//		System.out.println("type1Empty+"+type1Empty);
//		System.out.println("type2Empty+"+type2Empty);
//		System.out.println("type3Empty+"+type3Empty);
		List<Map<RoomType, Integer>> listRoomSchedule = new ArrayList<>();
		
		int fourPeopleRoom = peopleAmount/4; 
		int twoPeopleRoom =0;
		if(peopleAmount%4 > 0) {
			twoPeopleRoom = (int)Math.ceil((double)((double)(peopleAmount%4)/2));}
		if(twoPeopleRoom >=2) {
			fourPeopleRoom += (twoPeopleRoom/2);
			twoPeopleRoom = twoPeopleRoom%2;
		}
		
//		System.out.println("peopleAmount+"+peopleAmount);
//		System.out.println("fourPeopleRoom+"+fourPeopleRoom);
//		System.out.println("twoPeopleRoom+"+twoPeopleRoom);
		
		
		if(fourPeopleRoom>0 && twoPeopleRoom>0) {
			if(type2Empty>=fourPeopleRoom && type1Empty>=twoPeopleRoom) {
				Map<RoomType, Integer> element = new LinkedHashMap<>();
				element.put(rtSvc.getOnebyId(1), twoPeopleRoom);
				element.put(rtSvc.getOnebyId(2), fourPeopleRoom);			
				listRoomSchedule.add(element);
			}
			if(type3Empty>=fourPeopleRoom && type1Empty>=twoPeopleRoom) {
				Map<RoomType, Integer> element = new LinkedHashMap<>();
				element.put(rtSvc.getOnebyId(1), twoPeopleRoom);
				element.put(rtSvc.getOnebyId(3), fourPeopleRoom);			
				listRoomSchedule.add(element);
			}
			if(type2Empty<fourPeopleRoom && type3Empty<fourPeopleRoom && type2Empty+type3Empty>=fourPeopleRoom && type1Empty>=twoPeopleRoom){
				Map<RoomType, Integer> element1 = new LinkedHashMap<>();
				element1.put(rtSvc.getOnebyId(1), twoPeopleRoom);
				element1.put(rtSvc.getOnebyId(2), type2Empty);
				element1.put(rtSvc.getOnebyId(3), (fourPeopleRoom-type2Empty));			
				listRoomSchedule.add(element1);
				
				Map<RoomType, Integer> element2 = new LinkedHashMap<>();
				element2.put(rtSvc.getOnebyId(1), twoPeopleRoom);
				element2.put(rtSvc.getOnebyId(2), (fourPeopleRoom-type3Empty));
				element2.put(rtSvc.getOnebyId(3), type3Empty);			
				listRoomSchedule.add(element2);
				
			}else if(type2Empty+type3Empty == (fourPeopleRoom-1) && type1Empty-twoPeopleRoom==2) {
				Map<RoomType, Integer> element1 = new LinkedHashMap<>();
				element1.put(rtSvc.getOnebyId(1), twoPeopleRoom+2);
				element1.put(rtSvc.getOnebyId(2), type2Empty);
				element1.put(rtSvc.getOnebyId(3), (fourPeopleRoom-type2Empty-1));			
				listRoomSchedule.add(element1);
				
				Map<RoomType, Integer> element2 = new LinkedHashMap<>();
				element2.put(rtSvc.getOnebyId(1), twoPeopleRoom+2);
				element2.put(rtSvc.getOnebyId(2), (fourPeopleRoom-type3Empty-1));
				element2.put(rtSvc.getOnebyId(3), type3Empty);			
				listRoomSchedule.add(element2);
			}
			
		}else if(fourPeopleRoom>0 && twoPeopleRoom==0) {
			if(type2Empty>=fourPeopleRoom) {
				Map<RoomType, Integer> element = new LinkedHashMap<>();
				element.put(rtSvc.getOnebyId(2), fourPeopleRoom);			
				listRoomSchedule.add(element);
			}
			if(type3Empty>=fourPeopleRoom) {
				Map<RoomType, Integer> element = new LinkedHashMap<>();
				element.put(rtSvc.getOnebyId(3), fourPeopleRoom);			
				listRoomSchedule.add(element);
			}
			if(type2Empty<fourPeopleRoom && type3Empty<fourPeopleRoom && type2Empty+type3Empty>=fourPeopleRoom){
				Map<RoomType, Integer> element1 = new LinkedHashMap<>();
				element1.put(rtSvc.getOnebyId(2), type2Empty);
				element1.put(rtSvc.getOnebyId(3), (fourPeopleRoom-type2Empty));			
				listRoomSchedule.add(element1);
				
				Map<RoomType, Integer> element2 = new LinkedHashMap<>();
				element2.put(rtSvc.getOnebyId(2), (fourPeopleRoom-type3Empty));
				element2.put(rtSvc.getOnebyId(3), type3Empty);			
				listRoomSchedule.add(element2);
			}else if(type2Empty+type3Empty == (fourPeopleRoom-1) && type1Empty>=2) {
				if(type2Empty==0) {
					Map<RoomType, Integer> element = new LinkedHashMap<>();
					element.put(rtSvc.getOnebyId(1), 2);
					element.put(rtSvc.getOnebyId(3), (fourPeopleRoom-1));			
					listRoomSchedule.add(element);
				}else if(type3Empty==0) {
					Map<RoomType, Integer> element = new LinkedHashMap<>();
					element.put(rtSvc.getOnebyId(1), 2);
					element.put(rtSvc.getOnebyId(2), (fourPeopleRoom-1));			
					listRoomSchedule.add(element);
				}else {
					Map<RoomType, Integer> element1 = new LinkedHashMap<>();
					element1.put(rtSvc.getOnebyId(1), 2);
					element1.put(rtSvc.getOnebyId(2), type2Empty);
					element1.put(rtSvc.getOnebyId(3), (fourPeopleRoom-type2Empty-1));			
					listRoomSchedule.add(element1);
					
					Map<RoomType, Integer> element2 = new LinkedHashMap<>();
					element2.put(rtSvc.getOnebyId(1), 2);
					element2.put(rtSvc.getOnebyId(2), (fourPeopleRoom-type3Empty-1));
					element2.put(rtSvc.getOnebyId(3), type3Empty);			
					listRoomSchedule.add(element2);
				}
				
			}
			
		}else if(fourPeopleRoom==0 && twoPeopleRoom>0 ) {
			if(type1Empty>=twoPeopleRoom) {
				Map<RoomType, Integer> element = new LinkedHashMap<>();
				element.put(rtSvc.getOnebyId(1), twoPeopleRoom);		
				listRoomSchedule.add(element);
			}else if(type1Empty<twoPeopleRoom && (type2Empty>0 || type3Empty>0)) {
				if(type2Empty==0) {
					Map<RoomType, Integer> element = new LinkedHashMap<>();
					element.put(rtSvc.getOnebyId(3), 1);		
					listRoomSchedule.add(element);
				}else if(type3Empty==0) {
					Map<RoomType, Integer> element = new LinkedHashMap<>();
					element.put(rtSvc.getOnebyId(2), 1);		
					listRoomSchedule.add(element);
				}else {
					Map<RoomType, Integer> element1 = new LinkedHashMap<>();
					element1.put(rtSvc.getOnebyId(2), 1);		
					listRoomSchedule.add(element1);
					
					Map<RoomType, Integer> element2 = new LinkedHashMap<>();
					element2.put(rtSvc.getOnebyId(3), 1);
					listRoomSchedule.add(element2);
				}
			}
			
		}
		System.out.println("listRoomSchedule+"+listRoomSchedule);
		
		return listRoomSchedule;
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