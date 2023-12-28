package com.example.attendance.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.attendance.contants.RtnCode;
import com.example.attendance.entity.Department;

public class DepartmentSearchRes {
	
	List<Department> DepartmentList = new ArrayList<>();
	
	private  RtnCode rtnCode;

	public DepartmentSearchRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentSearchRes(List<Department> departmentList, RtnCode rtnCode) {
		super();
		DepartmentList = departmentList;
		this.rtnCode = rtnCode;
	}

	public DepartmentSearchRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public List<Department> getDepartmentList() {
		return DepartmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		DepartmentList = departmentList;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	

}
