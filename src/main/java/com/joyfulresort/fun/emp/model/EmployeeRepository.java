//package com.joyfulresort.emp.model;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
//
//}



package com.joyfulresort.fun.emp.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {
	 
}
