package com.example.attendance;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.attendance.entity.Department;
import com.example.attendance.repository.DepartmentDao;
import com.example.attendance.service.ifs.DepartmentService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.DepartmentCreateReq;
import com.example.attendance.vo.DepartmentSearchRes;

@SpringBootTest
public class departmentTest {


	@Autowired
	private DepartmentService service;
	
	private DepartmentDao dao;

	// only for initial初始建立用
	@Test
	public void adminDepTest() {
		Department dep = new Department("99", "Admin");
		DepartmentCreateReq req = new DepartmentCreateReq(Arrays.asList(dep));
		BasicRes res = service.create(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Department create error!!");

	}
	
	@Test
	public void createDepTest() {
		DepartmentCreateReq req = new DepartmentCreateReq(Arrays.asList(
				new Department("01", "HR"),
				new Department("02", "IT")));
		BasicRes res = service.create(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Department create error!!");
	}
	
	@Test
	public void searchAlldepartmentTest() {
		DepartmentSearchRes res = service.searchAllDepartment();
		System.out.println(res.getRtnCode());
	}

}
