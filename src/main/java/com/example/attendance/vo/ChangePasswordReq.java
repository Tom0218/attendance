package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordReq {

	private String id;

	@JsonProperty("old_password")
	private String oldpwd;

	@JsonProperty("auth_code")
	private String authCode;

	@JsonProperty("new_password")
	private String newpwd;

	public ChangePasswordReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChangePasswordReq(String id, String authCode, String newpwd) {
		super();
		this.id = id;
		this.authCode = authCode;
		this.newpwd = newpwd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

}
