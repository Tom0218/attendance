package com.example.attendance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="departments")
public class Department {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "name")
	private String name;

	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Department(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
