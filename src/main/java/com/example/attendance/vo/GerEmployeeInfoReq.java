package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GerEmployeeInfoReq {

	@JsonProperty("caller_Id")
	private String callerId;
	
	@JsonProperty("target_Id")
	private String targertId;

	public String getCallerId() {
		return callerId;
	}

	
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	
	public String getTargertId() {
		return targertId;
	}

	public void setTargertId(String targertId) {
		this.targertId = targertId;
	}
	
	
}
