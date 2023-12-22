package com.example.attendance.vo;

import java.util.List;

import com.example.attendance.entity.Department;

public class DepartmentCreateReq{
	
	//一次新增多個部門
	private List<Department> depList;

	public DepartmentCreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentCreateReq(List<Department> depList) {
		super();
		this.depList = depList;
	}

	public List<Department> getDepList() {
		return depList;
	}

	public void setDepList(List<Department> depList) {
		this.depList = depList;
	}
	
	

}
