package com.example.attendance.vo;

import java.time.LocalDate;

import com.example.attendance.entity.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeCreateReq extends Employee{

	private String creatorId;

	private String id;

	private String department;

	private String name;

	@JsonProperty("password")
	private String pwd;

	private String email;

	private boolean active;

	private String jobPosition;

	private LocalDate birthDate;

	private LocalDate arrivalDate;

	public EmployeeCreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getCreatorId() {
		return creatorId;
	}

	public EmployeeCreateReq(
			String creatorId, 
			String id, 
			String department, 
			String name, 
			String pwd, 
			String email,
			boolean active, 
			String jobPosition, 
			LocalDate birthDate, 
			LocalDate arrivalDate) {
		super();
		this.creatorId = creatorId;
		this.id = id;
		this.department = department;
		this.name = name;
		this.pwd = pwd;
		this.email = email;
		this.active = active;
		this.jobPosition = jobPosition;
		this.birthDate = birthDate;
		this.arrivalDate = arrivalDate;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

}
