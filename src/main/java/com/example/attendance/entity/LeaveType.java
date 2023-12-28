package com.example.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leave_type")
public class LeaveType {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "type")
	private int type;

	public LeaveType() {
		super();
	}

	public LeaveType(int id, int type) {
		super();
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
