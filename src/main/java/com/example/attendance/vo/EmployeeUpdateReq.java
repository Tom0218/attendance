package com.example.attendance.vo;

import java.time.LocalDate;

import com.example.attendance.entity.Employee;

public class EmployeeUpdateReq extends Employee{
	
	private String executorId;

	public EmployeeUpdateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeUpdateReq(String id, String department, String name, String pwd, String email, boolean active,
			String jobPosition, LocalDate birthDate, LocalDate arrivalDate) {
		super(id, department, name, pwd, email, active, jobPosition, birthDate, arrivalDate);
		// TODO Auto-generated constructor stub
	}

	public EmployeeUpdateReq(String id, String department, String name, String pwd, String email, String jobPosition,
			LocalDate birthDate, LocalDate arrivalDate, LocalDate resignationDate, String quitReason, boolean active,
			int annualLeave, int sickLeave) {
		super(id, department, name, pwd, email, jobPosition, birthDate, arrivalDate, resignationDate, quitReason, active,
				annualLeave, sickLeave);
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
