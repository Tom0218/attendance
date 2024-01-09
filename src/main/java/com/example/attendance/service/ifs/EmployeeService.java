package com.example.attendance.service.ifs;

import javax.servlet.http.HttpSession;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;
import com.example.attendance.vo.LoginRes;

public interface EmployeeService {

	public BasicRes create(EmployeeCreateReq req);
	
	public BasicRes create(Employee Employee);

	public LoginRes login(String id, String pwd, HttpSession session);

	public BasicRes changePassword(String id, String oldpwd, String newpwd);

	public BasicRes forgotPassword(String id, String email);

	public BasicRes changePasswordByAuthCode(String id, String authCode, String newpwd);

	public BasicRes activate(String executorId, String employeeId);

	// 停用
	public BasicRes deactivate(String executorId, String employeeId);

	// 同時可以達到 activate 和 deactivate的效用
	public BasicRes updateActive(String executorId, String employeeId, boolean isActive);

	public BasicRes updateResign(String executorId, String employeeId);

	public BasicRes resingApplication(String employeeId);

	public BasicRes updateInfo(String executorId, Employee employee);
	
	public EmployeeRes search(String id, String name, String department);

	public EmployeeRes findByEmployeeId(String employeeId);

	public EmployeeRes findStaffInfo(String callerId, String targetId);
	
	public EmployeeRes findSupervisor(String department);
	
	

}
