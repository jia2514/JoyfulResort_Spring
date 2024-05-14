package com.joyfulresort.fun.emp.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service("employeeService")
public class EmployeeService {
	
	@Autowired
	EmployeeRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public void addEmp(Employee employee) {
        employee = repository.save(employee); // 第一次保存，生成員工編號
        employee.setEmpAccount(employee.getEmpno()); // 將員工編號設置為員工帳號
        repository.save(employee); // 再次保存員工，更新員工帳號
    }
	
	
	public void updateEmp(Employee employee) {
		repository.save(employee);
	}
	
	public Employee getOneEmp(Integer empno) {
		Optional<Employee> optional = repository.findById(empno);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public List<Employee> getAll() {
		return repository.findAll();
	}
	

	
	public List<Employee> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
	}
	
	
	public Page<Employee> findAll(Pageable pageable) {
	    return repository.findAll(pageable);
	}
	
	
}
