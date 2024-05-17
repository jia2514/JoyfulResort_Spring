package com.joyfulresort.jia.roomtypephoto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joyfulresort.yu.roomtypephoto.model.RoomTypePhoto;

public interface RoomTypePhotoRepositoryJia extends JpaRepository<RoomTypePhoto, Integer> {

	@Query(nativeQuery = true, value = "SELECT room_type_photo_id FROM room_type_photo WHERE room_type_id =?1")
	List<Integer> findByRoomTypeId(Integer roomTypeId);
	
}
