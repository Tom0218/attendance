package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;

public class BasicRes {

	public RtnCode rtnCode;

	public BasicRes() {
		super();
	}

	public BasicRes(RtnCode rtnCode) {
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
