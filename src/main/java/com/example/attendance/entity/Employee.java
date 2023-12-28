package com.example.attendance.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "department")
	private String department;

	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String pwd;

	@Column(name = "email")
	private String email;

	@Column(name = "job_position")
	private String jobPosition;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Column(name = "arrival_date")
	private LocalDate arrivalDate;

	@Column(name = "resignation_date")
	private LocalDate resignationDate;

	@Column(name = "quit_reason")
	private String quitReason;

	@Column(name = "active")
	private boolean active;

	@Column(name = "annual_leave")
	private int annualLeave;

	@Column(name = "sick_leave")
	private int sickLeave = 30;

	public Employee() {
		super();
	}

	public Employee(String id, String department, String name, String pwd, String email, String jobPosition,
			LocalDate birthDate, LocalDate arrivalDate, LocalDate resignationDate, String quitReason, boolean active,
			int annualLeave, int sickLeave) {
		super();
		this.id = id;
		this.department = department;
		this.name = name;
		this.pwd = pwd;
		this.email = email;
		this.jobPosition = jobPosition;
		this.birthDate = birthDate;
		this.arrivalDate = arrivalDate;
		this.resignationDate = resignationDate;
		this.quitReason = quitReason;
		this.active = active;
		this.annualLeave = annualLeave;
		this.sickLeave = sickLeave;
	}

	public Employee(String id, String department, String name, String pwd, String email, boolean active,
			String jobPosition, LocalDate birthDate, LocalDate arrivalDate) {
		super();
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

	public LocalDate getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(LocalDate resignationDate) {
		this.resignationDate = resignationDate;
	}

	public String getQuitReason() {
		return quitReason;
	}

	public void setQuitReason(String quitReason) {
		this.quitReason = quitReason;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getAnnualLeave() {
		return annualLeave;
	}

	public void setAnnualLeave(int annualLeave) {
		this.annualLeave = annualLeave;
	}

	public int getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(int sickLeave) {
		this.sickLeave = sickLeave;
	}

}
