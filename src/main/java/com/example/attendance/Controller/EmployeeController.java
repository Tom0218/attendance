package com.example.attendance.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.ChangePasswordReq;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;
import com.example.attendance.vo.EmployeeUpdateReq;
import com.example.attendance.vo.ForgotPasswordReq;
import com.example.attendance.vo.GetEmployeeInfoReq;
import com.example.attendance.vo.LoginReq;
import com.example.attendance.vo.LoginRes;

@RestController
@CrossOrigin // 串前端
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "api/attendance/login")
	public LoginRes login(@RequestBody LoginReq req, HttpSession session) {
		// 確認是否有帳號登入
		if (session.getAttribute(req.getId()) == null) {
			return service.login(req.getId(), req.getPwd(), session);
		}
		System.out.println(session.getId());
		return new LoginRes(RtnCode.PARAM_ERROR);
	}

	@GetMapping(value = "api/attendance/login1")
	public LoginRes login1(@RequestParam(value = "id") String id, @RequestParam(value = "password") String pwd,
			HttpSession session) {
		if (session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}

		return new LoginRes(RtnCode.PARAM_ERROR);
	}

	@PostMapping(value = "api/attendance/logout")
	public BasicRes logout(HttpSession session) {
		System.out.println(session.getId());
		if (session != null) {
			session.invalidate();
			System.out.println("logout successul!!");
			System.out.println(session.getId());
			return new BasicRes(RtnCode.SUCCESSFUL);
		}
		// 讓整個session失效
		return new BasicRes(RtnCode.PARAM_ERROR);
	}

	@PostMapping(value = "api/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		System.out.println(session.getId());
		// 登入
		System.out.println("DEPATEMENT:" + session.getAttribute(req.getCreatorId()));
		System.out.println("name:" + req.getName());
		System.out.println("pwd:" + req.getPwd());
		if ((session.getAttribute(req.getCreatorId())) == null) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		// 權限檢查
		if (!session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
//		if (!session.getAttribute("").toString().equalsIgnoreCase("admin")) {
//			return new BasicRes(RtnCode.UNAUTHORIZATED);
//		}
		Employee employee = new Employee(req.getId(), req.getDepartment(), req.getName(), req.getPwd(), req.getEmail(),
				req.isActive(), req.getJobPosition(), req.getBirthDate(), req.getArrivalDate());
		return service.create(employee);
	}

	public BasicRes updateEmployeeInfo(@RequestBody EmployeeUpdateReq req) {
		return null;
	}

	@PostMapping(value = "api/attendance/employee/updateActive")
	public BasicRes updateActive(@RequestParam(value = "executorId") String executorId,
			@RequestParam(value = "employeeId") String employeeId, @RequestParam(value = "isActive") boolean isActive) {
		System.out.println(executorId + employeeId + isActive);
		return service.updateActive(executorId, employeeId, isActive);
	}

	@PostMapping(value = "api/attendance/employee/change_password")
	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		if (session.getAttribute(req.getId()) == null) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		return service.changePassword(req.getId(), req.getOldpwd(), req.getNewpwd());
	}

	@PostMapping(value = "api/attendance/employee/forgot_password")
	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {
		return service.forgotPassword(req.getId(), req.getEmail());
	}

	@PostMapping(value = "api/attendance/employee/change_password_by_auth_code")
	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {
		System.out.println(req.getId() + req.getNewpwd() + req.getAuthCode());
		return service.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewpwd());
	}

	// 取得自己的資訊
	@PostMapping(value = "api/attendance/employee/get_info")
	public EmployeeRes findByEmployeeId(@RequestBody GetEmployeeInfoReq req, HttpSession session) {
		// 判斷登入是否成功
		if (session.getAttribute(req.getCallerId()) == null) {
			return new EmployeeRes(RtnCode.PLEASE_LOGIN_FIRST, null);
		}
		return service.findByEmployeeId(req.getCallerId());
	}

	@GetMapping(value = "api/employee/search")
	public EmployeeRes searchEmployee(@RequestParam String id, @RequestParam String name,
			@RequestParam String department) {
//		System.out.println("name:" + name);
//		System.out.println("id:" + id);
		return service.search(id, name, department);
	}
	
	@GetMapping(value = "api/employee/SearchSupervisor")
	public EmployeeRes SearchSupervisor(@RequestParam String department) {
		return service.findSupervisor(department);
	}

}
