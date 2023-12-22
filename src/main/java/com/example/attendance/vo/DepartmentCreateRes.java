package com.example.attendance.vo;

import com.example.attendance.contants.RtnCode;

public class DepartmentCreateRes {
	
	private  RtnCode rtnCode;

	public DepartmentCreateRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentCreateRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	

}
