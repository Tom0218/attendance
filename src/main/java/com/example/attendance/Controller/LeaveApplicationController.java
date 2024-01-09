package com.example.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

@RestController
@CrossOrigin // ¦ê«eºÝ
public class LeaveApplicationController {
	
	@Autowired
	private LeaveApplicationService  service;
	
	@PostMapping(value = "api/attendance/leaveApplication/apply")
	public BasicRes apply(LeaveApplicationReq req) {
		return service.apply(req);
	}

}
