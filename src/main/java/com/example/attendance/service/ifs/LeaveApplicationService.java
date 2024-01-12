package com.example.attendance.service.ifs;

import java.util.List;

import com.example.attendance.entity.LeaveApplication;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

public interface LeaveApplicationService {

	public BasicRes apply(LeaveApplicationReq req);
	
	public List<LeaveApplication> searchAllApply(String ReviewerId, String applicationNo, String employeeId);
	
	public BasicRes review(String reviewId, String applicationNo);
}
