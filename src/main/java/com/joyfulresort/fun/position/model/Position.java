package com.joyfulresort.fun.position.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.joyfulresort.fun.emp.model.Employee;
import com.joyfulresort.fun.positionauthority.model.PositionAuthority;


@Entity
@Table(name="position")
public class Position {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="position_id")
	private Integer positionId;
	
	

	@NotBlank(message = "職務名稱不能為空")
	@Pattern(regexp = "^[\u4e00-\u9fa5]{2,10}$", message = "職務名稱: 只能是中文, 且長度必需在2到10之間")
	@Column(name="position_name", unique = true)
	//	@Column(name="position_name")
	private String  positionName;

	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
	@OrderBy("empno asc")  //資料庫的欄位名稱
	private Set<Employee> emps;


	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
	@OrderBy("position_id asc")
	private Set<PositionAuthority> positionAuthority;
	
	
	public Position() {
		super();
	}


	public Position(Integer positionId, String positionName, Set<Employee> emps, Set<PositionAuthority> positionAuthority) {
		super();
		this.positionId = positionId;
		this.positionName = positionName;
		this.emps = emps;
		this.positionAuthority = positionAuthority;
	}


	public Integer getPositionId() {
		return positionId;
	}


	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}


	public String getPositionName() {
		return positionName;
	}


	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}


	public Set<Employee> getEmps() {
		return emps;
	}


	public void setEmps(Set<Employee> emps) {
		this.emps = emps;
	}


	public Set<PositionAuthority> getAuthorities() {
		return positionAuthority;
	}


	public void setAuthorities(Set<PositionAuthority> positionAuthority) {
		this.positionAuthority = positionAuthority;
	}

	
	public int getEmployeeCount() {
	    return emps != null ? emps.size() : 0;
	}

	
	
	public String toString() {
		return "Position[positionId="+positionId+", positionName="+positionName+"]";
	}
	
	
	


	
	


	
	
}