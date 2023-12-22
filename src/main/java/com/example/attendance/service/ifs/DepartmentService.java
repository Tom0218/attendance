package com.example.attendance.service.ifs;

import com.example.attendance.vo.DepartmentCreateReq;
import com.example.attendance.vo.BasicRes;

public interface DepartmentService {
	
	public BasicRes create(DepartmentCreateReq req);

}
