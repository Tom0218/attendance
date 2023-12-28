package com.example.attendance.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.service.ifs.DepartmentService;
import com.example.attendance.vo.DepartmentSearchRes;

@RestController
public class DepartmentController {
	
	@Autowired
	private DepartmentService Service;
	
	@GetMapping(value = "api/department/searchAll")
	public DepartmentSearchRes searchAll() {
		return Service.searchAllDepartment();
	}
	
	

}
