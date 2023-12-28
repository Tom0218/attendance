package com.example.attendance.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.contants.RtnCode;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.ChangePasswordReq;
import com.example.attendance.vo.EmployeeCreatReq;
import com.example.attendance.vo.EmployeeRes;
import com.example.attendance.vo.ForgotPasswordReq;
import com.example.attendance.vo.GerEmployeeInfoReq;
import com.example.attendance.vo.LoginReq;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@PostMapping(value = "api/attendance/login")
	// requestionbody須將id與pwd包成物件
	// 單獨帶入的話用@RequestParam，getmapping
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		if (session.getAttribute(req.getId()) == null) {
			return service.login(req.getId(), req.getPwd(), session);
		}
		session.setAttribute("id", req.getId());
		session.setAttribute("password", req.getPwd());
		session.setMaxInactiveInterval(300);// 設定session有效時間，單位:second
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@GetMapping(value = "api/attendance/login1")
	public BasicRes login1(@RequestParam(value = "id") String id, @RequestParam(value = "password") String pwd,
			HttpSession session) {
		if (session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@GetMapping(value = "api/attendance/logout")
	public BasicRes logout(HttpSession session) {
		session.invalidate();
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@PostMapping(value = "api/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreatReq req, HttpSession session) {
		// 以session判斷是否已登入
		if (session.getAttribute(req.getCreatorId()) == null) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		// 判斷登入者權限可否create
		if (!session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		return service.create(req);

	}

	// 需登入
	@PostMapping(value = "api/attendance/employee/change_password")
	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		if (session.getAttribute(req.getId()) == null) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		return service.changePassword(req.getId(), req.getOldPwd(), req.getNewPwd());
	}

	@PostMapping(value = "api/attendance/employee/forgot_password")
	public BasicRes forgotPassword1(@RequestBody ForgotPasswordReq req) {
		return service.forgotPassword(req.getId(), req.getEmail());
	}

	@PostMapping(value = "api/attendance/employee/change_password_by_auth_code")
	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {
		return service.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewPwd());
	}
	
	//取得自己的資訊
	@PostMapping("api/attendance/employee/get_info")
	public EmployeeRes findByEmployeeId(@RequestBody GerEmployeeInfoReq req, HttpSession session) {
		if (session.getAttribute(req.getCallerId()) == null) {
			return new EmployeeRes(RtnCode.PLEASE_LOGIN_FIRST,null);
		}
		return service.findByEmployeeId(req.getCallerId());
	}

}
