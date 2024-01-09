package com.example.attendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentSearchRes;

@RestController
@CrossOrigin	//¦ê«eºÝ
public class DepartmentController {
	
	@Autowired
	private DepartmentsService Service;
	
	@GetMapping(value = "api/department/searchAll")
	public DepartmentSearchRes searchAll() {
		return Service.searchAll();
	}

}
