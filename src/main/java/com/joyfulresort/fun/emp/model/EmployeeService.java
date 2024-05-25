package com.joyfulresort.fun.emp.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.joyfulresort.fun.empmail.model.MailService;
import com.joyfulresort.fun.empmail.model.TokenStore;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp3;

@Service("employeeService")
public class EmployeeService {

	@Autowired
	EmployeeRepository repository;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	MailService mailService;

	@Autowired
	TokenStore tokenStore;

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
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<Employee> getAll() {
		return repository.findAll();
	}

	public List<Employee> getAll(Map<String, String[]> map) {
		return HibernateUtil_CompositeQuery_Emp3.getAllC(map, sessionFactory.openSession());
	}

	public Page<Employee> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public boolean verifyEmployee(Integer empAccount, String empName, String positionName) {
		Optional<Employee> employeeOpt = repository.findById(empAccount);
		if (employeeOpt.isPresent()) {
			Employee employee = employeeOpt.get();
			return employee.getEmpName().equals(empName)
					&& employee.getPosition().getPositionName().equals(positionName);
		}
		return false;
	}

	@Transactional
	public boolean sendPasswordResetLink(String empEmail, Integer empAccount) {
		Optional<Employee> employeeOpt = repository.findById(empAccount);
		if (employeeOpt.isPresent()) {
			Employee employee = employeeOpt.get();
			String token = UUID.randomUUID().toString();
			tokenStore.storeToken(token, empAccount);
			String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
			String resetUrl = baseUrl + "/passwordReset/reset-password?token=" + token;
			String subject = "密碼重設請求";
			String message = "請點擊以下連結重設您的密碼: " + resetUrl;
			mailService.sendMail(empEmail, subject, message);
			return true;
		}
		return false;
	}

	@Transactional
	public boolean resetPassword(String token, String newPassword) {
	    Integer empAccount = tokenStore.getEmpAccountByToken(token);
	    if (empAccount != null) {
	        Optional<Employee> optionalEmployee = repository.findById(empAccount);
	        if (optionalEmployee.isPresent()) {
	            Employee employee = optionalEmployee.get();
	            employee.setEmpPassword(newPassword); // 在這裡進行密碼加密
	            tokenStore.removeToken(token); // 移除 token
	            repository.save(employee);
	            return true;
	        }
	    }
	    return false;
	}
}
