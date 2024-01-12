package com.example.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.entity.LeaveApplication;
import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

@RestController
@CrossOrigin // ¦ê«eºÝ
public class LeaveApplicationController {
	
	@Autowired
	private LeaveApplicationService  service;
	
	@PostMapping(value = "api/attendance/leaveApplication/apply")
	public BasicRes apply(@RequestBody LeaveApplicationReq req) {
		return service.apply(req);
	}
	
	@GetMapping(value = "api/attendance/leaveApplication/findApply")
	public List<LeaveApplication> searchAllApply(@RequestParam String reviewerId,@RequestParam String applicationNo, @RequestParam String employeeId) {
		return service.searchAllApply(reviewerId,applicationNo,employeeId);
	}

}
