package com.joyfulresort.fun.emp.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.joyfulresort.fun.position.model.Position;

@Entity
@Table(name="emp")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "EMPNO", updatable = false)
	private Integer empno;
	
	
//	@Column(name="positioniid")
//	private Integer positioniId;
	
	
	@NotBlank(message = "員工名字不能為空")
	@Pattern(regexp = "^[a-zA-Z\\u4e00-\\u9fa5]*$", message = "員工名字只能包含英文大小寫和中文！")
	@Column(name="emp_name")
	private String empName;
	
	@NotNull(message = "入職日期不能為空")
	@PastOrPresent(message = "入職日期必須是過去或當前日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="hire_date")
	private LocalDate hiredate;
	
	@Column(name="emp_state")
	private Boolean empState;
	
//	@NotNull(message = "員工帳號不能為空")
	@Column(name="emp_account")
	private Integer empAccount;
	
	@NotBlank(message = "員工密碼不能為空")
	@Pattern(regexp = "^[a-zA-Z0-9_]{6,20}$", message = "員工密碼只能包含字母、數字和_，且長度必需在6到20之間")
	@Column(name="emp_password")
	private String empPassword;
	
	@Column(name="image",  columnDefinition = "longblob")
	private byte[] image;
	
	@NotNull(message = "請選擇一個職位")
//	@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "position_id", referencedColumnName = "position_id")
	private Position position;

	public Employee() {
		super();
	}

	public Employee(Integer empno, String empName, LocalDate hiredate, Boolean empState, Integer empAccount,
			String empPassword, byte[] image, Position position) {
		super();
		this.empno = empno;
		this.empName = empName;
		this.hiredate = hiredate;
		this.empState = empState;
		this.empAccount = empAccount;
		this.empPassword = empPassword;
		this.image = image;
		this.position = position;
	}

	public Integer getEmpno() {
		return empno;
	}

	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public LocalDate getHiredate() {
		return hiredate;
	}

	public void setHiredate(LocalDate hiredate) {
		this.hiredate = hiredate;
	}

	public Boolean getEmpState() {
		return empState;
	}

	public void setEmpState(Boolean empState) {
		this.empState = empState;
	}

	public Integer getEmpAccount() {
		return empAccount;
	}

	public void setEmpAccount(Integer empAccount) {
		this.empAccount = empAccount;
	}

	public String getEmpPassword() {
		return empPassword;
	}

	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	
	@Override
	public String toString() {
		return "Employee [empno=" + empno +", positionName=" + position.getPositionName() +  ", empName=" + empName + ", hiredate=" + hiredate + ", empState=" + empState + ", empAccount=" + empAccount
				+ ", empPassword=" + empPassword + "]";
	}

	
	
	
	
}
