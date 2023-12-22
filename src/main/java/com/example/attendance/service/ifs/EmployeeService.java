package com.example.attendance.service.ifs;

import javax.servlet.http.HttpSession;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreatReq;
import com.example.attendance.vo.EmployeeRes;

public interface EmployeeService {
	
	public BasicRes create(EmployeeCreatReq req);
	
	public BasicRes login(String id, String password, HttpSession session);
	
	public BasicRes changePassword(String id, String oldPwd, String newPwd);
	
	public BasicRes forgotPassword(String id, String email);

	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd);
	
	//啟用
	public BasicRes activate(String excutorId, String employeeId );
	
	//停用
	public BasicRes deactivate(String excutorId, String employeeId );
	
	//啟用與停用*以isActive判斷 true:啟用，false:停用
	public BasicRes updateActive(String excutorId, String employeeId ,boolean isActive);
	
	public BasicRes updateResign(String executorId, String employeeId);
	
	public BasicRes resignApplication( String employeeId);
	
	public BasicRes updateInfo(String executorId, Employee employee);
	
	public EmployeeRes findByEmployeeId(String employeeId);
	
	public EmployeeRes findStaffInfo(String callerId, String targetId);
	
}
