package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;

public class DepartmentsCreateRes {

	public RtnCode rtnCode;
	

	public DepartmentsCreateRes() {
		super();
	}

	public DepartmentsCreateRes(RtnCode rtnCode) {
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
