package com.example.attendance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.attendance.repository.LeaveApplicationDao;
import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.LeaveApplicationReq;

@SpringBootTest
public class LeaveApplicationTest {
	
	@Autowired
	private LeaveApplicationService service;
	
	@Autowired
	private LeaveApplicationDao dao;
	
	@Test
	public void findAllLeaveApplyTest() {
		System.out.println(dao.findByReviewerId("tom","20240112201913336715100",""));
	}
	

}
