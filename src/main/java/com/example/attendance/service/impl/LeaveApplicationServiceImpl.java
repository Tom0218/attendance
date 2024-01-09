package com.example.attendance.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.LeaveType;
import com.example.attendance.constants.ReviewType;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.LeaveApplication;
import com.example.attendance.entity.Mail;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.LeaveApplicationDao;
import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private LeaveApplicationDao dao;

	@Override
	public BasicRes apply(LeaveApplicationReq req) {
		if (LeaveType.parser(req.getEmployeeDepartment()) == null) {
			return new BasicRes(RtnCode.LEAVE_TYPE_ERROR);
		}
		if (req.getLeaveStartDatetime().isAfter(req.getLeaveEndDatetime()) || req.getAppliedDatetime() == null) {
			return new BasicRes(RtnCode.LEAVE_APPLIED_DATETIME_ERROR);
		}
		if (!StringUtils.hasText(req.getLeaveReason())) {
			return new BasicRes(RtnCode.LEAVE_REASON_CANNOT_BE_EMPTY);
		}
		if (!StringUtils.hasText(req.getReviewerId())) {
			return new BasicRes(RtnCode.LEAVE_REASON_ID_CANNOT_BE_EMPTY);
		}
		if(LeaveType.needCertification(req.getLeaveType())
				&& req.getCertification() == null) {
			return new BasicRes(RtnCode.LACK_CERTIFICATION);
		}
		Optional<Employee> op = employeeDao.findById(req.getReviewerId());
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.LEAVE_REASON_ID_NOT_FOUND);
		}
		Employee reviewer = op.get();
		if (!JobPosition.hasGetStaffInfoPermission(reviewer.getJobPosition())) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}
		LocalDateTime now = LocalDateTime.now();
		req.setApplicationNo(now.toString().replaceAll("[-T:.]", ""));
		req.setUpdateDatetime(now);
		try {
			dao.save((LeaveApplication) req);
			// 記 email 給 reviewer
			Mail.sentLeaveApplyMail(reviewer.getEmail());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLICATION_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes review(String reviewId, String applicationNo) {
		if (!StringUtils.hasText(applicationNo)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		List<LeaveApplication> list = dao.findByApplicationNo(applicationNo);
		if (CollectionUtils.isEmpty(list)) {
			return new BasicRes(RtnCode.LEAVE_APPLICATION_NOT_FOUND);
		}
		// 取的最新假單
		LeaveApplication application = list.get(list.size() - 1);
		Employee reviewer = employeeDao.findById(reviewId).get();
		// 假單申請者要與審核員同部門
		if (!application.getEmployeeDepartment().equalsIgnoreCase(reviewer.getDepartment())) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}
		// 假單審核的權限要室主管以上
		if (!JobPosition.hasGetStaffInfoPermission(reviewer.getJobPosition())) {
			return new BasicRes(RtnCode.PERMISSION_DENIED);
		}
		if(LeaveType.needCertification(application.getLeaveType())
				&& application.getCertification() == null) {
			application.setReviewerStatus(ReviewType.REJECT.getType());
			application.setReviewerStatus(RtnCode.LACK_CERTIFICATION.getMessage());
		}else {
			application.setReviewerStatus(ReviewType.PASS.getType());
		}
		LocalDateTime now = LocalDateTime.now();
		application.setReviewerDatetime(now);
//		application.setReviewerStatus(ReviewType.PASS.getType());
		application.setUpdateDatetime(now);
		try {
			dao.save(new LeaveApplication(application));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLIED_DATETIME_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

}
