package com.joyfulresort.fun.positionauthority.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joyfulresort.fun.authorityfunction.model.AuthorityFunctionService;
import com.joyfulresort.fun.position.model.PositionService;


@Service("positionAuthorityService")
public class PositionAuthorityService {
	
	@Autowired
	PositionAuthorityRepository repository;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	AuthorityFunctionService authorityFunctionService;
	
//	public void addpositionAuthority(PositionAuthority positionAuthority) {
//		repository.save(positionAuthority);
//	}
	
	
	@Transactional
	public void addPositionAuthorities(Integer positionId, List<Integer> functionIds) {
	    for (Integer functionId : functionIds) {
	        PositionAuthority positionAuthority = new PositionAuthority();
	        positionAuthority.setCompositeKey(new PositionAuthority.CompositeDetail(positionId, functionId));
	        positionAuthority.setPosition(positionService.findPositionById(positionId)); // 確保這個方法能正確獲取實體
	        positionAuthority.setAuthorityFunction(authorityFunctionService.getOneAuthorityFunction(functionId)); // 同上
	        // 創建複合主鍵ID
	        PositionAuthority.CompositeDetail id = new PositionAuthority.CompositeDetail(positionId, functionId);
	        
	        
	        // 檢查該職權關聯是否已存在
	        if (!repository.existsById(id)) {
	            repository.save(positionAuthority); // 儲存每個新的職務與權限關聯
	          
	        }
	    }
	}

	
	@Transactional
    public void updatePositionAuthorities(Integer positionId, List<Integer> functionIds) {
        // 刪除該職位的所有現有權限
        repository.deleteByPositionId(positionId);

        // 添加新的權限
        for (Integer functionId : functionIds) {
	        PositionAuthority positionAuthority = new PositionAuthority();
	        positionAuthority.setCompositeKey(new PositionAuthority.CompositeDetail(positionId, functionId));
	        positionAuthority.setPosition(positionService.findPositionById(positionId)); // 確保這個方法能正確獲取實體
	        positionAuthority.setAuthorityFunction(authorityFunctionService.getOneAuthorityFunction(functionId)); // 同上
	        // 創建複合主鍵ID
	        PositionAuthority.CompositeDetail id = new PositionAuthority.CompositeDetail(positionId, functionId);
	        
	       
	        // 檢查該職權關聯是否已存在
	        if (!repository.existsById(id)) {
	            repository.save(positionAuthority); // 儲存每個新的職務與權限關聯
	            
	        }
	    }
    }
	
	
	
//	public void updatepositionAuthority(PositionAuthority positionAuthority) {
//		repository.save(positionAuthority);
//	}

	
	public void deletePositionAuthority(PositionAuthority.CompositeDetail id) {
	    repository.deleteById(id);
	}

	
	public PositionAuthority getPositionAuthority(PositionAuthority.CompositeDetail id) {
	    return repository.findById(id).orElse(null);
	}

	public List<PositionAuthority> getAllPositionAuthoritiesSorted() {
	    return repository.findAll(Sort.by(Sort.Direction.ASC, "positionId"));
	}

}
