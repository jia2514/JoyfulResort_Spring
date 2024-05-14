package com.joyfulresort.fun.position.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.joyfulresort.fun.position.model.Position;
import com.joyfulresort.fun.position.model.PositionService;


@RestController
public class PositionController {

	
	@Autowired
    private PositionService positionService;

	
	//原版
    @PostMapping("/positions/insert")
    public ResponseEntity<Map<String, Object>> insertPosition(@RequestBody Position position) {
        System.out.println("測試點新增職務-----------------------------------");
    	return positionService.insertPosition(position);
    }
    
    // 新增的修改職務方法
    @PostMapping("/positions/update")
    public ResponseEntity<Map<String, Object>> updatePosition(@RequestBody Position position) {
    	   System.out.println("測試點修改職務-----------------------------------");
    	   System.out.println("Received position ID: " + position.getPositionId());
    	   System.out.println("Received position Name: " + position.getPositionName());

    	if (position.getPositionId() == null) {
            // 若未提供職務 ID，則返回一個錯誤響應
            return ResponseEntity.badRequest().body(Map.of("error", "Position ID is required for update"));
        }
        System.out.println("正在修改職務，職務ID：" + position.getPositionId());
        return positionService.updatePosition(position);
    }
    
    
    // 刪除職務的方法
    @DeleteMapping("/positions/delete/{positionId}")
    public ResponseEntity<Map<String, Object>> deletePosition(@PathVariable Integer positionId) {
        System.out.println("測試點刪除職務-----------------------------------");
        System.out.println("Received position ID for deletion: " + positionId);

        // 調用service層的方法來刪除職務
        try {
            positionService.deletePosition(positionId);
            return ResponseEntity.ok(Map.of("success", true, "message", "職務已成功刪除"));
        } catch (Exception e) {
            // 如果職務不能被刪除（例如因為還有員工在這個職務上），捕獲異常並返回錯誤訊息
            System.err.println("刪除職務出錯: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("success", false, "errorMessage", e.getMessage()));
        }
    }
   
	
}
