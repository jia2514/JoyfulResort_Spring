package com.joyfulresort.fun.position.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("positionService")
public class PositionService {

	@Autowired
	PositionRepository repository;

	@Autowired
	private SessionFactory sessionFactory;


//	public void updatePosition(Position position) {
//		repository.save(position);
//	}

	public Position findPositionById(Integer positionId) {
        return repository.findById(positionId).orElse(null);
    }

	public List<Position> findAllPositions() {
		return repository.findAll();
	}
	
	
//	 @Transactional
//	    public void deletePosition(Integer positionId) {
//	        Position position = repository.findById(positionId).orElse(null);
//	        if (position != null) {
//	            if (!position.getEmps().isEmpty() || !position.getAuthorities().isEmpty()) {
//	                throw new RuntimeException("不能刪除，因為還存在關聯的員工或權限");
//	            }
//	            repository.delete(position);
//	        }
//	    }
	 

	@Transactional
	public ResponseEntity<Map<String, Object>> deletePosition(Integer positionId) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        Position position = repository.findById(positionId).orElse(null);
	        if (position == null) {
	            response.put("success", false);
	            response.put("errorMessage", "職務不存在");
	            return ResponseEntity.badRequest().body(response);
	        }

	        // 檢查職務下是否有員工
	        if (position.getEmps() != null && !position.getEmps().isEmpty()) {
	            response.put("success", false);
	            response.put("errorMessage", "無法刪除，該職務下仍有員工");
	            return ResponseEntity.badRequest().body(response);
	        }

	        // 執行刪除操作
	        repository.delete(position);
	        response.put("success", true);
	        response.put("message", "職務已成功刪除");
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("errorMessage", "刪除失敗: " + e.getMessage());
	        return ResponseEntity.internalServerError().body(response);
	    }
	    return ResponseEntity.ok(response);
	}

	
	
	
	 
	 //原本二
	 public ResponseEntity<Map<String, Object>> insertPosition(Position position) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		        Optional<Position> existingPosition = repository.findByPositionName(position.getPositionName());
		        if (existingPosition.isPresent()) {
		            response.put("success", false);
		            response.put("errorMessage", "已有該職務名稱");
		        } else {
		            Position savedPosition = repository.save(position); // 保存職務並接收返回的實例
		            response.put("success", true);
		            response.put("positionId", savedPosition.getPositionId()); // 返回新職務的 ID
		            response.put("positionName", savedPosition.getPositionName()); // 返回新職務的名稱
		            response.put("employeeCount", savedPosition.getEmployeeCount()); // 確保這個方法存在並返回正確的數量
		        }
		    } catch (Exception e) {
		        response.put("success", false);
		         response.put("errorMessage", "職務名稱必須是中文，且長度在2到10之間");
		    }
		    return ResponseEntity.ok(response);
		}
	 
	 
	 public List<Map<String, Object>> getAllPositions() {
		    List<Position> positions = repository.findAll();
		    List<Map<String, Object>> positionsWithCounts = new ArrayList<>();
		    for (Position position : positions) {
		        Map<String, Object> positionData = new HashMap<>();
		        positionData.put("positionId", position.getPositionId());
		        positionData.put("positionName", position.getPositionName());
		        positionData.put("employeeCount", position.getEmployeeCount());  // 调用 getEmployeeCount 方法
		        positionsWithCounts.add(positionData);
		    }
		    return positionsWithCounts;
		}

	 
	 
	 
	 
	 @Transactional
	 public ResponseEntity<Map<String, Object>> updatePosition(Position position) {
	     Map<String, Object> response = new HashMap<>();
	     try {
	         Position existingPosition = repository.findById(position.getPositionId()).orElse(null);
	         if (existingPosition == null) {
	             response.put("success", false);
	             response.put("errorMessage", "職務不存在");
	             return ResponseEntity.badRequest().body(response);
	         }
	         
	         // 檢查職務名稱是否符合規定的格式
	         if (!position.getPositionName().matches("^[\u4e00-\u9fa5]{2,10}$")) {
	             response.put("success", false);
	             response.put("errorMessage", "職務名稱必須是中文，且長度在2到10之間");
	             return ResponseEntity.badRequest().body(response);
	         }
	         
	         existingPosition.setPositionName(position.getPositionName()); // 確保更新職務名稱
	         Position updatedPosition = repository.save(existingPosition); // 保存更新

	         response.put("success", true);
	         response.put("positionId", updatedPosition.getPositionId());
	         response.put("positionName", updatedPosition.getPositionName());
	         response.put("employeeCount", updatedPosition.getEmps() != null ? updatedPosition.getEmps().size() : 0); // 更新員工數量
	     } catch (Exception e) {
	         response.put("success", false);
	         response.put("errorMessage", "更新失敗: " + e.getMessage());
	         return ResponseEntity.internalServerError().body(response);
	     }
	     return ResponseEntity.ok(response);
	 }



}
