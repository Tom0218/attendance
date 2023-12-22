package com.example.attendance.entity;

import java.sql.Blob;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "leave_application")
public class LeaveApplication {

	@Id
	@Column(name = "serial_no")
	private int serialNo;

	@Column(name = "application_no")
	private String applicationNo;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "employee_department")
	private String employeeDepartment;

	@Column(name = "leave_type")
	private String leaveType;

	@Column(name = "leave_start_date")
	private LocalDateTime leaveStartDate;

	@Column(name = "leave_end_date")
	private LocalDateTime leaveEndDate;

	@Column(name = "total_hours")
	private LocalDateTime totalHours;

	@Column(name = "leave_reason")
	private String leaveReason;

	@Column(name = "reviewer_id")
	private String reviewerId;

	@Column(name = "reviewer_datetime")
	private LocalDateTime reviewerDatetime;

	@Column(name = "reviewer_status")
	private String reviewerStatus;

	@Column(name = "reject_reason")
	private String rejectReason;

	@Column(name = "applied_datetime")
	private LocalDateTime appliedDatetime;

	@Column(name = "update_datetime")
	private LocalDateTime updateDatetime;
	
	@Column(name = "certification")
	private byte[] certification;

	public LeaveApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveApplication(String applicationNo, String employeeId, String employeeDepartment, String leaveType,
			LocalDateTime leaveStartDate, LocalDateTime leaveEndDate, LocalDateTime totalHours, String leaveReason,
			String reviewerId, LocalDateTime reviewerDatetime, String reviewerStatus, String rejectReason,
			LocalDateTime appliedDatetime, LocalDateTime updateDatetime) {
		super();
		this.applicationNo = applicationNo;
		this.employeeId = employeeId;
		this.employeeDepartment = employeeDepartment;
		this.leaveType = leaveType;
		this.leaveStartDate = leaveStartDate;
		this.leaveEndDate = leaveEndDate;
		this.totalHours = totalHours;
		this.leaveReason = leaveReason;
		this.reviewerId = reviewerId;
		this.reviewerDatetime = reviewerDatetime;
		this.reviewerStatus = reviewerStatus;
		this.rejectReason = rejectReason;
		this.appliedDatetime = appliedDatetime;
		this.updateDatetime = updateDatetime;
	}
	
	public LeaveApplication(LeaveApplication application) {
		super();
		this.applicationNo = application.getApplicationNo();
		this.employeeId = application.getEmployeeId();
		this.employeeDepartment = application.getEmployeeDepartment();
		this.leaveType = application.getLeaveType();
		this.leaveStartDate = application.getLeaveStartDate();
		this.leaveEndDate = application.getLeaveEndDate();
		this.totalHours = application.getTotalHours();
		this.leaveReason = application.getLeaveReason();
		this.reviewerId = application.getReviewerId();
		this.reviewerDatetime = application.getReviewerDatetime();
		this.reviewerStatus = application.getReviewerStatus();
		this.rejectReason = application.rejectReason;
		this.appliedDatetime = application.appliedDatetime;
		this.updateDatetime = application.updateDatetime;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDateTime getLeaveStartDate() {
		return leaveStartDate;
	}

	public void setLeaveStartDate(LocalDateTime leaveStartDate) {
		this.leaveStartDate = leaveStartDate;
	}

	public LocalDateTime getLeaveEndDate() {
		return leaveEndDate;
	}

	public void setLeaveEndDate(LocalDateTime leaveEndDate) {
		this.leaveEndDate = leaveEndDate;
	}

	public LocalDateTime getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(LocalDateTime totalHours) {
		this.totalHours = totalHours;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public LocalDateTime getReviewerDatetime() {
		return reviewerDatetime;
	}

	public void setReviewerDatetime(LocalDateTime reviewerDatetime) {
		this.reviewerDatetime = reviewerDatetime;
	}

	public String getReviewerStatus() {
		return reviewerStatus;
	}

	public void setReviewerStatus(String reviewerStatus) {
		this.reviewerStatus = reviewerStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public LocalDateTime getAppliedDatetime() {
		return appliedDatetime;
	}

	public void setAppliedDatetime(LocalDateTime appliedDatetime) {
		this.appliedDatetime = appliedDatetime;
	}

	public LocalDateTime getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(LocalDateTime updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public byte[] getCertification() {
		return certification;
	}

	public void setCertification(byte[] certification) {
		this.certification = certification;
	}

	

}
