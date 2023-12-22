package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordReq {
	
	private String id;
	
	@JsonProperty("new_passworrd")//for postman
	private String newPwd;
	
	@JsonProperty("old_passworrd")//for postman
	private String oldPwd;
	
	@JsonProperty("auth_code")//for postman
	private String authCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	

}
