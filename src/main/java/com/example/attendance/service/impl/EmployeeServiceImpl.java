package com.example.attendance.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.Mail;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.repository.AuthCodeDao;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.ResignApplicationDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;
import com.example.attendance.vo.LoginRes;

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
	public BasicRes create(Employee employee) {
		if (!StringUtils.hasText(employee.getId()) || !StringUtils.hasText(employee.getDepartment())
				|| !StringUtils.hasText(employee.getName()) || !StringUtils.hasText(employee.getPwd())
				|| !StringUtils.hasText(employee.getEmail()) || !StringUtils.hasText(employee.getJobPosition())
				|| employee.getArrivalDate() == null || employee.getBirthDate() == null) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		if (dao.existsById(employee.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		// check deparement_name
		if (!departmentsDao.existsByName(employee.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);
		}
		employee.setPwd(encoder.encode(employee.getPwd()));
		try {
			dao.save((Employee) employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	
	@Override
	public LoginRes login(String id, String pwd, HttpSession session) {
		System.out.println(id+pwd);
		if (!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new LoginRes(RtnCode.PARAM_ERROR);
		}
		Optional<Employee> op = dao.findById(id);
		if (op.isEmpty()) {
			return new LoginRes(RtnCode.ID_NOT_FOUND);
		}
		Employee employee = op.get();
		if (!encoder.matches(pwd, employee.getPwd())) {
			return new LoginRes(RtnCode.PASSWORD_ERROR);
		}
		if (!employee.isActive()) {
			return new LoginRes(RtnCode.ACCOUNT_DEACTIVATE);
		}
		session.setAttribute(id,employee.getDepartment());	// (A01,"IT") key ������
//		session.setAttribute("id",id);
		// ����
		session.setMaxInactiveInterval(3000);
		System.out.println(session.getId());
		logger.info("Login successfull");
		return new LoginRes(employee,RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldpwd, String newpwd) {
		System.out.println(id+oldpwd+newpwd);
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldpwd) || !StringUtils.hasText(newpwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		if (oldpwd.equals(newpwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);
		}
		if(dao.findById(id).isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		Employee employee = dao.findById(id).get();
		if (!encoder.matches(oldpwd, employee.getPwd())) {
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
		System.out.println(employee.getEmail());
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));
		// �������ҽX ( ���Įɶ�30����
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			dao.save(employee);
			authCodeDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));
			Mail.sentSignUpMail(employee.getEmail(),authCode,randomPwd);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		// ���� email
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

	// ��k���,�� updateActive ���N
	@Deprecated
	@Override
	public BasicRes activate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// ���ΧP�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// ��s���\ = 1 �S���\�N�| != 1
		if (dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// ��k���,�� updateActive ���N
	@Deprecated
	@Override
	public BasicRes deactivate(String executorId, String employeeId) {
		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// ���ΧP�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// ��s���\ = 1 �S���\�N�| != 1
		if (dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@CacheEvict(cacheNames = "updateActive", allEntries = true)
	@Override
	public BasicRes updateActive(String executorId, String employeeId, boolean isActive) {
		// ���� executorId �P�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// ���ΧP�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
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

	// HR �h��g���u��¾��
	@CacheEvict(cacheNames = "updateResign", allEntries = true)
	@Override
	public BasicRes updateResign(String executorId, String employeeId) {
		// ���� executorId �P�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// ���ΧP�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
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
		// ���� employeeId �P�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(
					new ResignApplication(employeeId, employee.getDepartment(), LocalDate.now().plusMonths(1), "¾���Q��"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@CacheEvict(cacheNames = "updateInfo", allEntries = true)
	@Override
	public BasicRes updateInfo(String executorId, Employee employee) {
		// ���� executorId �P�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
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

	@CacheEvict(cacheNames = "update", allEntries = true)
	@Override
	public EmployeeRes findByEmployeeId(String employeeId) {
		return new EmployeeRes(RtnCode.SUCCESSFUL, dao.findById(employeeId).get());
	}

	@CacheEvict(cacheNames = "update", allEntries = true)
	@Override
	public EmployeeRes findStaffInfo(String callerId, String targetId) {
		// ���� callerId �P�_�O�_���šA�]������k�����O login ��~�ϥ�, �blogin ��k���w���P�_
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
		// 1.�P�����B caller �O���D�� : 2.caller �O HR ����
		if ((callerDepartment.equals(targetInfo.getDepartment())
				&& JobPosition.hasGetStaffInfoPermission(callerInfo.getJobPosition()))
				|| callerDepartment.equalsIgnoreCase("HR")) {
			return new EmployeeRes(RtnCode.SUCCESSFUL, null);
		}
		return new EmployeeRes(RtnCode.PERMISSION_DENIED, null);
	}

	@CacheEvict(cacheNames = "update", allEntries = true)
	@Override
	public EmployeeRes search(String id, String name, String department) {
		id = StringUtils.hasText(id) ? id : "";
		name = StringUtils.hasText(name) ? name : "";
		department = StringUtils.hasText(department) ? department : "";
//		System.out.println("name:" + name);
		List<Employee> employeeList = dao.findEmployee(id, name, department);
		if (employeeList.isEmpty()) {
			return new EmployeeRes(RtnCode.EMPLOYEE_NOT_FOUND);
		}
		return new EmployeeRes(employeeList, RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes create(EmployeeCreateReq req) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public EmployeeRes findSupervisor(String department) {
		List<Employee> employeeList = dao.findSupervisor(department);
		return new EmployeeRes(employeeList,RtnCode.SUCCESSFUL);
	}



	

}
