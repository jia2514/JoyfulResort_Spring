package com.joyfulresort.fun.authorityfunction.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("authorityFunctionService")
public class AuthorityFunctionService {
	
	@Autowired
	AuthorityFunctionRepository repository;
	
	
	public void addAuthorityFunction(AuthorityFunction authorityFunction) {
		repository.save(authorityFunction);
	}
	
	
	public void updateAuthorityFunction(AuthorityFunction authorityFunction){
		repository.save(authorityFunction);
	}

	public AuthorityFunction getOneAuthorityFunction(Integer functionId) {
	Optional<AuthorityFunction> optional	= repository.findById(functionId);
	return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	

	
	
	public Page<AuthorityFunction> findAll(Pageable pageable) {
	    return repository.findAll(pageable);
	}
	
	public boolean existsByName(String functionName) {
	    return repository.existsByFunctionName(functionName);
	}

	
	 public void deleteAuthorityFunctionById(Integer functionId) {
	        repository.deleteById(functionId);
	    }
	 
	 
	 
		public List<AuthorityFunction> getAll(){
			return repository.findAll();
		}
	 
	 public List<Map<String, Object>> getAllAuthorityFunctionsAsMap() {
	        List<AuthorityFunction> authorityFunctions = repository.findAll();
	        List<Map<String, Object>> authorityFunctionMaps = new ArrayList<>();
	        
	        for (AuthorityFunction authorityFunction : authorityFunctions) {
	            Map<String, Object> authorityFunctionMap = new HashMap<>();
	            authorityFunctionMap.put("functionId", authorityFunction.getFunctionId());
	            authorityFunctionMap.put("functionName", authorityFunction.getFunctionName());
	            // 根据需要添加更多属性
	            authorityFunctionMaps.add(authorityFunctionMap);
	        }
	        return authorityFunctionMaps;
	    }
	 
}



