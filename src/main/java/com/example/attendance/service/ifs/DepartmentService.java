package com.example.attendance.service.ifs;

import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.DepartmentCreateReq;
import com.example.attendance.vo.DepartmentSearchRes;

public interface DepartmentService {
	
	public BasicRes create(DepartmentCreateReq req);
	
	public  DepartmentSearchRes searchAllDepartment();

}
