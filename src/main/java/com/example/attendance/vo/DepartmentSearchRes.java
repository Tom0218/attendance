package com.example.attendance.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;

public class DepartmentSearchRes {
	
	public RtnCode rtnCode;
	
	List<Departments>departments = new ArrayList<>();

	public DepartmentSearchRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentSearchRes(RtnCode rtnCode, List<Departments> departments) {
		super();
		this.rtnCode = rtnCode;
		this.departments = departments;
	}

	public DepartmentSearchRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

	public List<Departments> getDepartments() {
		return departments;
	}

	public void setDepartment(List<Departments> departments) {
		this.departments = departments;
	}
	
	

}
