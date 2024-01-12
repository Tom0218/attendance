package com.example.attendance.vo;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import com.example.attendance.entity.LeaveApplication;

public class LeaveApplicationReq extends LeaveApplication {

	public LeaveApplicationReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveApplicationReq(String applicationNo, String employeeId, String employeeDepartment, String leaveType,
			LocalDateTime leaveStartDatetime, LocalDateTime leaveEndDatetime, int totalHour, String leaveReason,
			String reviewerId, LocalDateTime appliedDatetime, LocalDateTime updateDatetime) {
		super(applicationNo, employeeId, employeeDepartment, leaveType, leaveStartDatetime, leaveEndDatetime, totalHour,
				leaveReason, reviewerId, appliedDatetime, updateDatetime);
		// TODO Auto-generated constructor stub
	}

	public LeaveApplicationReq(int serialNo, String applicationNo, String employeeId, String employeeDepartment,
			String leaveType, LocalDateTime leaveStartDatetime, LocalDateTime leaveEndDatetime, int totalHour,
			String leaveReason, String reviewerId, LocalDateTime reviewDatetime, String reviewStatus,
			String rejectReason, LocalDateTime appliedDatetime, LocalDateTime updateDatetime, byte[] certification) {
		super(serialNo, applicationNo, employeeId, employeeDepartment, leaveType, leaveStartDatetime, leaveEndDatetime,
				totalHour, leaveReason, reviewerId, reviewDatetime, reviewStatus, rejectReason, appliedDatetime,
				updateDatetime, certification);
		// TODO Auto-generated constructor stub
	}

	public LeaveApplicationReq(LeaveApplication application) {
		super(application);
		// TODO Auto-generated constructor stub
	}

	public LeaveApplicationReq(String employeeId, String employeeDepartment, String leaveType,
			LocalDateTime leaveStartDatetime, LocalDateTime leaveEndDatetime, int totalHour, String leaveReason,
			String reviewerId, LocalDateTime appliedDatetime) {
		super(employeeId, employeeDepartment, leaveType, leaveStartDatetime, leaveEndDatetime, totalHour, leaveReason,
				reviewerId, appliedDatetime);
		// TODO Auto-generated constructor stub
	}

}
