package com.joyfulresort.reservesession.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.joyfulresort.reserveorder.model.ResVO;

@Entity
@Table(name = "reserve_session")
public class RessionVO implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reserve_session_id")
	private Integer reserveSessionId;

	@Column(name = "reserve_max_part")
	private Integer reserveMaxPart = 50;

	@Column(name = "session_time", length = 10)
	private String sessionTime;

	@OneToMany(mappedBy = "ressionVO", cascade = CascadeType.ALL)
	private Set<ResVO> reserveOrders = new HashSet<>();

	public Integer getReserveSessionId() {
		return reserveSessionId;
	}

	public void setReserveSessionId(Integer reserveSessionId) {
		this.reserveSessionId = reserveSessionId;
	}

	public Integer getReserveMaxPart() {
		return reserveMaxPart;
	}

	public void setReserveMaxPart(Integer reserveMaxPart) {
		this.reserveMaxPart = reserveMaxPart;
	}

	public String getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}

	public Set<ResVO> getReserveOrders() {
		return reserveOrders;
	}

	public void setReserveOrders(Set<ResVO> reserveOrders) {
		this.reserveOrders = reserveOrders;
	}

	 

	public RessionVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ReserveSession [reserveSessionId=" + reserveSessionId + ", reserveMaxPart=" + reserveMaxPart
				+ ", sessionTime=" + sessionTime + ", reserveOrders=" + reserveOrders + "]";
	}

}
