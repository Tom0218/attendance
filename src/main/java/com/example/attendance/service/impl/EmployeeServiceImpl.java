package com.example.attendance.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.repository.AuthCodeDao;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.ResignApplicationDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

import net.bytebuddy.utility.RandomString;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	
	@Autowired
	private EmployeeDao dao;

	@Autowired
	private DepartmentsDao departmentsDao;

	@Autowired
	private AuthCodeDao authCodeDao;

	@Autowired
	private ResignApplicationDao resignApplicationDao;

	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public BasicRes create(EmployeeCreateReq req) {
		if (!StringUtils.hasText(req.getId()) || !StringUtils.hasText(req.getDepartment())
				|| !StringUtils.hasText(req.getName()) || !StringUtils.hasText(req.getPwd())
				|| !StringUtils.hasText(req.getEmail()) || !StringUtils.hasText(req.getJobPosition())
				|| req.getArrivalDate() == null || req.getBirthDate() == null) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		if (dao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		// check deparement_name
		if (!departmentsDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}
		req.setPwd(encoder.encode(req.getPwd()));
		try {
			dao.save((Employee) req);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public BasicRes login(String id, String pwd, HttpSession session) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		Optional<Employee> op = dao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		Employee employee = op.get();
		if (!encoder.matches(pwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		if (!employee.isActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVATE);
		}
		session.setAttribute(id, employee.getDepartment()); // (A01,"IT") key 部門值
		// 單位秒
		session.setMaxInactiveInterval(3000);
		logger.info("Login successfull");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldpwd, String newpwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldpwd) || !StringUtils.hasText(newpwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		if (oldpwd.equals(newpwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);
		}
		Employee employee = dao.findById(id).get();
		if (encoder.matches(oldpwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		employee.setPwd(encoder.encode(newpwd));
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes forgotPassword(String id, String email) {
		if (!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		Employee employee = null;
		if (StringUtils.hasText(id)) {
			Optional<Employee> op = dao.findById(id);
			if (op.isEmpty()) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
			employee = op.get();
		} else {
			employee = dao.findByEmail(email);
			if (employee == null) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
		}
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));
		// 產生驗證碼 ( 有效時間30分鐘
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			dao.save(employee);
			authCodeDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		// 間接 email
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode, String newpwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newpwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// check auth code
		Optional<AuthCode> op = authCodeDao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		AuthCode authCodeEntity = op.get();
		if (!authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCHED);
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(authCodeEntity.getAuthDatetime())) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);
		}
		Employee employee = dao.findById(id).get();
		employee.setPwd(encoder.encode(newpwd));
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// 方法棄用,由 updateActive 取代
	@Deprecated
	@Override
	public BasicRes activate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 不用判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// 更新成功 = 1 沒成功就會 != 1
		if (dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// 方法棄用,由 updateActive 取代
	@Deprecated
	@Override
	public BasicRes deactivate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 不用判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// 更新成功 = 1 沒成功就會 != 1
		if (dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateActive(String executorId, String employeeId, boolean isActive) {
		// 不用 executorId 判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 不用判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		try {
			int res = dao.updateActivate(employeeId, isActive);
			if (res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}

		if (dao.updateActivate(employeeId, isActive) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// HR 去填寫員工離職單
	@Override
	public BasicRes updateResign(String executorId, String employeeId) {
		// 不用 executorId 判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 不用判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		Employee executor = dao.findById(employeeId).get();
		if (!executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		ResignApplication application = resignApplicationDao.findByEmployeeId(employeeId);
		Employee employee = dao.findById(employeeId).get();
		employee.setResignationDate(application.getResignationDate());
		employee.setQuitReason(application.getQuitReason());
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes resingApplication(String employeeId) {
		// 不用 employeeId 判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(
					new ResignApplication(employeeId, employee.getDepartment(), LocalDate.now().plusMonths(1), "職場霸凌"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public BasicRes updateInfo(String executorId, Employee employee) {
		// 不用 executorId 判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		if (!StringUtils.hasText(employee.getId()) || executorId.equals(employee.getId())) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		try {
			int res = dao.updateInfo(employee.getId(), employee.getDepartment(), employee.getName(),
					employee.getEmail(), employee.getJobPosition(), employee.getBirthDate(), employee.getArrivalDate(),
					employee.getAnnualLeave(), employee.getSickLeave());
			if (res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public EmployeeRes findByEmployeeId(String employeeId) {
		return new EmployeeRes(RtnCode.SUCCESSFUL, dao.findById(employeeId).get());
	}

	@Override
	public EmployeeRes findStaffInfo(String callerId, String targetId) {
		// 不用 callerId 判斷是否為空，因為此方法必須是 login 後才使用, 在login 方法那已有判斷
		if (!StringUtils.hasText(targetId)) {
			return new EmployeeRes(RtnCode.PARAM_ERROR, null);
		}
		Optional<Employee> op = dao.findById(callerId);
		if (op.isEmpty()) {
			return new EmployeeRes(RtnCode.ID_NOT_FOUND, null);
		}
		Employee targetInfo = op.get();
		Employee callerInfo = dao.findById(callerId).get();
		String callerDepartment = callerInfo.getDepartment();
		// 1.同部門且 caller 是單位主管 : 2.caller 是 HR 部門
		if ((callerDepartment.equals(targetInfo.getDepartment())
				&& JobPosition.hasGetStaffInfoPermission(callerInfo.getJobPosition()))
				|| callerDepartment.equalsIgnoreCase("HR")) {
			return new EmployeeRes(RtnCode.SUCCESSFUL, null);
		}
		return new EmployeeRes(RtnCode.PERMISSION_DENIED, null);
	}

}
