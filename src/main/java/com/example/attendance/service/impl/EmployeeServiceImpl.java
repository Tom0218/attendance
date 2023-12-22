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

import com.example.attendance.contants.JobPosition;
import com.example.attendance.contants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.repository.AuthCodeDao;
import com.example.attendance.repository.DepartmentDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.ResignApplicationDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreatReq;
import com.example.attendance.vo.EmployeeRes;

import net.bytebuddy.utility.RandomString;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	//slf4j
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;

	@Autowired
	private ResignApplicationDao resignApplicationDao;

	@Autowired
	private EmployeeDao dao;

	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private AuthCodeDao authCodeDao;

	@Override
	public BasicRes create(EmployeeCreatReq req) {
		if (!StringUtils.hasText(req.getId()) || StringUtils.hasText(req.getName())
				|| !StringUtils.hasText(req.getDepartment()) || StringUtils.hasText(req.getPwd())
				|| !StringUtils.hasText(req.getEmail()) || !StringUtils.hasText(req.getJobPosition())
				|| req.getArrivalDate() == null || req.getBirthDate() == null) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		if (dao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		// check depqrtment_name
		if (departmentDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}
		req.setPwd(encoder.encode(req.getPwd()));
		try {
			dao.save((Employee) req);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.EMPLOYEE_CREATE_ERROR);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public BasicRes login(String id, String pwd, HttpSession session) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// chick id & pwd
		Optional<Employee> op = dao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		Employee employee = op.get();
		if (!encoder.matches(pwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		if (!employee.isActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIIVATE);
		}
		session.setAttribute(id,employee.getDepartment());//("A01","IT)"
		
		session.setMaxInactiveInterval(3000);// session失效時間，單為(秒)
		logger.info("login successful!!");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 判斷新舊密碼是否相同
		if (oldPwd.equals(newPwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_INDENTICAL);
		}
		// 不用判斷是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee employee = dao.findById(id).get();
		// 判斷就密碼是否正確
		if (!encoder.matches(oldPwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		employee.setPwd(encoder.encode(newPwd));
		// 若try發生異常，錯誤的話執行catch內容
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes forgotPassword(String id, String email) {
		if (!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
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
		// 產生隨機字串*驗證碼
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));

		// 產生驗證碼，有效時間為30min
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			dao.save(employee);
			// authCodeExpiredTime至appliction properties更改
			authCodeDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}

		// 界接email並寄送auth_code
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// check auth_code
		Optional<AuthCode> op = authCodeDao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		AuthCode authcodeEntity = op.get();
		if (!authcodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTHCODE_NOT_MATCHED);
		}
		// 判斷驗證碼時效
		LocalDateTime now = LocalDateTime.now();
		// 判段是否在有效時間之後
		if (now.isAfter((authcodeEntity.getAuthDateTime()))) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);
		}
		Employee employee = dao.findById(id).get();
		employee.setPwd(encoder.encode(newPwd));
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	// 啟用帳號
	// @Deprecated ，已棄用，由updateActive取代
	@Deprecated
	@Override
	public BasicRes activate(String executorId, String employeeId) {
		/// 檢查excutorId、employeeId是否為空、是否是更新自己狀態
		// 不用判斷executor是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 檢查id
		// 不用判斷是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// true啟用
		// 若更新成功為1，因為Dao設int若成功則會回傳大於零的數字
		if (dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// 停用帳號，已棄用，由updateActive取代
	@Deprecated
	@Override
	public BasicRes deactivate(String excutorId, String employeeId) {
		// 不用判斷executor是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if (!StringUtils.hasText(employeeId) || excutorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 檢查id
		// 不用判斷是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee executor = dao.findById(excutorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// false停用
		// 若更新成功為1，因為Dao設int若成功則會回傳大於零的數字
		if (dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateActive(String excutorId, String employeeId, boolean isActive) {
		// 不用判斷executor是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if (!StringUtils.hasText(employeeId) || excutorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 檢查id
		// 不用判斷是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee executor = dao.findById(excutorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// 若更新成功為1，因為Dao設int若成功則會回傳大於零的數字
		if (dao.updateActivate(employeeId, isActive) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateResign(String executorId, String employeeId) {
		// 不用判斷executor是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// 不用判斷是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("HR")) {
			// 不是管理員或HR部門的使用者，權限不足
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		ResignApplication application = resignApplicationDao.findByEmployeeId(employeeId);
		Employee employee = dao.findById(employeeId).get();
		employee.setResignationDate(LocalDate.now().plusMonths(1));
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
	public BasicRes resignApplication(String employeeId) {
		// 不用判斷employeeId是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(
					new ResignApplication(employeeId, employee.getDepartment(), LocalDate.now().plusMonths(1), "不爽做了"));
		} catch (Exception e) {
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateInfo(String executorId, Employee employee) {
		// 不用判斷executor是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if (!StringUtils.hasText(employee.getId()) || executorId.equals(employee.getId())) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		try {
			int res = dao.updateInfo(
					employee.getId(),
					employee.getDepartment(),
					employee.getName(),
					employee.getEmail(),
					employee.getJobPosition(),
					employee.getBirthDate(),
					employee.getArrivalDate(),
					employee.getAnnualLeave(),
					employee.getSickLeave());
			if (res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public EmployeeRes findByEmployeeId(String employeeId) {
		// 不用判斷employeeId是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		return new EmployeeRes(RtnCode.SUCCESSFUL, dao.findById(employeeId).get());
	}

	@Override
	public EmployeeRes findStaffInfo(String callerId, String targetId) {
		// 不用判斷callerId是否為空，因此方法必須是login後才能使用，在login方法那已有判斷
		if(!StringUtils.hasText(targetId)) {
			return new EmployeeRes(RtnCode.PARAM_ERROR,null);
		}
		 Optional<Employee> op = dao.findById(targetId);
		 if(op.isEmpty()) {
			 return new EmployeeRes(RtnCode.ID_NOT_FOUND,null);
		 }
		 Employee targetInfo = op.get();
		 Employee callerInfo = dao.findById(callerId).get(); 
		 String callerDepartment = callerInfo.getDepartment();
//		 1.判斷同部門且caller是單位主管;2.caller是 HR 部門
//		 and會先做
		 if((callerDepartment.equals(targetInfo.getDepartment()) &&
				 JobPosition.hasReviewPermission(callerInfo.getJobPosition())) ||
				 callerDepartment.equalsIgnoreCase("HR"))  {
			 return new EmployeeRes(RtnCode.SUCCESSFUL,targetInfo);
		 }
		 return new EmployeeRes(RtnCode.PERMISSION_DENIED,null);
	}

}
