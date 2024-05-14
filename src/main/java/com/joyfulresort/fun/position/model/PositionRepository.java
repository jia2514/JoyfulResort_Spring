package com.joyfulresort.fun.position.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer>{
	 // 根據需要，這裡可以添加更多自定義方法
	

	// 添加一個根據職務名稱查詢職務的方法
    Optional<Position> findByPositionName(String positionName);

	
	
}
