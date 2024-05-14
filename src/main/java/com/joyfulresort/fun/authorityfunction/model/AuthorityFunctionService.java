package com.joyfulresort.fun.authorityfunction.model;

import java.util.List;
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
	
	
	public List<AuthorityFunction> getAll(){
		return repository.findAll();
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
}
