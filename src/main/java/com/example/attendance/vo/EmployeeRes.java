package com.example.attendance.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;

public class EmployeeRes extends BasicRes {

	private Employee employee;

	private List<Employee> employeeList = new ArrayList<>();

	public RtnCode rtnCode;

	public EmployeeRes() {
		super();
	}

	public EmployeeRes(List<Employee> employeeList, RtnCode rtnCode) {
		super();
		this.employeeList = employeeList;
		this.rtnCode = rtnCode;
	}

	public EmployeeRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public EmployeeRes(RtnCode rtnCode, Employee employee) {
		super(rtnCode);
		this.employee = employee;
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

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	
	

}
