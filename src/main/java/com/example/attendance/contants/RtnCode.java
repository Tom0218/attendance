package com.example.attendance.contants;

public enum RtnCode {

	SUCCESSFUL(200, "Successful!!"),//
	PARAM_ERROR(400,"Param error !!"),//
	ID_HAS_EXISTED(400,"Id has existed!!"),//
	DEPARTMENT_NOT_FOUND(404,"Department not found!!"),//
	ID_NOT_FOUND(404,"Id not found!!"),//
	PASSWORD_ERROR(400,"Password error!!"),//
	EMPLOYEE_CREATE_ERROR(400,"Employee create error!!"),//
	PLEASE_LOGIN_FIRST(400,"Please login first!!"),//
	UNAUTHORIZATED(401,"Unauthorizated!!"),//
	CHANGE_PASSWORD_ERROR(40,"Change password error!!"),//
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_INDENTICAL(400,"Old password and new password are identical!!"),//
	FORGOT_PASSWORD_ERROR(400,"Forgot password error!!"),//
	AUTHCODE_NOT_MATCHED(400,"Authcode not matched!!"),//
	AUTH_CODE_EXPIRED(400,"authCode expired!!"),//
	UPDATE_FAILED(400,"Update failed!!"),//
	UPDATE_ERROR(400,"Update error!!"),//
	ACCOUNT_DEACTIIVATE(400,"Account deactivate!!"),//
	LEAVE_TYPE_ERROR(400,"Leave type error!!"),//
	LEAVE_APPLIED_DATETIME_ERROR(400,"Leave type error!!"),//
	LEAVE_REASON_CANNOT_EMPTY(400,"Leave reason cannot be empty!!"),
	LEAVE_REVIEWER_ID_CANNOT_BE_EMPTY(400,"Leave reviewer id cannot be empty!!"),
	LEAVE_REVIEWER_ID_NOT_FOUND(400,"Leave reviewer id not found!!"),
	LEAVE_APPLICATION_ERROR(400,"Leave application error!!"),
	LEAVE_APPLICATION_NOT_FOUND(400,"Leave application not found!!"),
	PERMISSION_DENIED(403,"Permission denied"),//
	LACK_CERTIFICATION(400,"Lack certification"),//
	;
	

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
