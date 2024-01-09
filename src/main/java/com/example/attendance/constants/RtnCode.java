package com.example.attendance.constants;

//列舉
public enum RtnCode {

	// 200 只 ok 的意思
	SUCCESSFUL(200, "successful"), //
	SEARCH_DEPARTMENT_SUCCESSFUL(200, "successful"), //
	PARAM_ERROR(400, "Param error"), //
	ID_HAS_EXISTED(400, "Id has existed"), //
	DEPARTMENT_NOT_FOUND(404, "Department not found"), //
	ID_NOT_FOUND(404, "Id not found"), //
	PASSWORD_ERROR(400, "Password erroe"), //
	EMPLOYEE_CREATE_ERROR(400, "Employee create erroe"), //
	EMPLOYEE_NOT_FOUND(400,"Employee not found "),//
	PLEASE_LOGIN_FIRST(400, "Please login first"), //
	UNAUTHORIZATED(401, "Unauthorizated"), //
	CHANGE_PASSWORD_ERROE(401, "Change password error"), //
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL(400, "Old password and new password are identical"), //
	FORGOT_PASSWORD_ERROE(400, "Forgot password error"), //
	AUTH_CODE_NOT_MATCHED(400, "Auth code not mathed"), //
	AUTH_CODE_EXPIRED(400, "Auth code expired"), //
	UPDATE_FAILED(400, "Update failed"), //
	ACCOUNT_DEACTIVATE(400, "Account deactivate"), //
	UPDATE_ERROR(400, "Update error"), //
	LEAVE_TYPE_ERROR(400, "Leave type error"), //
	LEAVE_APPLIED_DATETIME_ERROR(400, "Leave applied datetime error"), //
	LEAVE_REASON_CANNOT_BE_EMPTY(400, "Leave reason cannot empty"), //
	LEAVE_REASON_ID_CANNOT_BE_EMPTY(400, "Leave reason id cannot empty"), //
	LEAVE_REASON_ID_NOT_FOUND(400, "Leave reason id not found"), //
	PERMISSION_DENIED(403, "Permission denied "), //
	LEAVE_APPLICATION_ERROR(400, "Leave application error "), //
	LEAVE_APPLICATION_NOT_FOUND(400, "Leave application not found "), //
	LACK_CERTIFICATION(400, "Lack certification "), //

	;

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	// 只用 get 而已
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
