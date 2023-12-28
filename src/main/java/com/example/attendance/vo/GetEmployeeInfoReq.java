package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetEmployeeInfoReq {

	@JsonProperty("caller_id")
	private String callerId;

	@JsonProperty("target_id")
	private String targetId;

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}
