package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;

public class LoginRes {

	private Employee employee;

	public RtnCode rtnCode;

	public LoginRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public LoginRes(Employee employee, RtnCode rtnCode) {
		super();
		this.employee = employee;
		this.rtnCode = rtnCode;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	

}
