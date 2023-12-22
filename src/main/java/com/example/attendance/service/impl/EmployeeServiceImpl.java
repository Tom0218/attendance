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
		
		session.setMaxInactiveInterval(3000);// session���Įɶ��A�欰(��)
		logger.info("login successful!!");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// �P�_�s�±K�X�O�_�ۦP
		if (oldPwd.equals(newPwd)) {
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_INDENTICAL);
		}
		// ���ΧP�_�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee employee = dao.findById(id).get();
		// �P�_�N�K�X�O�_���T
		if (!encoder.matches(oldPwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		employee.setPwd(encoder.encode(newPwd));
		// �Ytry�o�Ͳ��`�A���~���ܰ���catch���e
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
		// �����H���r��*���ҽX
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));

		// �������ҽX�A���Įɶ���30min
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			dao.save(employee);
			// authCodeExpiredTime��appliction properties���
			authCodeDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}

		// �ɱ�email�ñH�eauth_code
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
		// �P�_���ҽX�ɮ�
		LocalDateTime now = LocalDateTime.now();
		// �P�q�O�_�b���Įɶ�����
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

	// �ҥαb��
	// @Deprecated �A�w��ΡA��updateActive���N
	@Deprecated
	@Override
	public BasicRes activate(String executorId, String employeeId) {
		/// �ˬdexcutorId�BemployeeId�O�_���šB�O�_�O��s�ۤv���A
		// ���ΧP�_executor�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// �ˬdid
		// ���ΧP�_�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// true�ҥ�
		// �Y��s���\��1�A�]��Dao�]int�Y���\�h�|�^�Ǥj��s���Ʀr
		if (dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// ���αb���A�w��ΡA��updateActive���N
	@Deprecated
	@Override
	public BasicRes deactivate(String excutorId, String employeeId) {
		// ���ΧP�_executor�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		if (!StringUtils.hasText(employeeId) || excutorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// �ˬdid
		// ���ΧP�_�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee executor = dao.findById(excutorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// false����
		// �Y��s���\��1�A�]��Dao�]int�Y���\�h�|�^�Ǥj��s���Ʀr
		if (dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateActive(String excutorId, String employeeId, boolean isActive) {
		// ���ΧP�_executor�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		if (!StringUtils.hasText(employeeId) || excutorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// �ˬdid
		// ���ΧP�_�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee executor = dao.findById(excutorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		// �Y��s���\��1�A�]��Dao�]int�Y���\�h�|�^�Ǥj��s���Ʀr
		if (dao.updateActivate(employeeId, isActive) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateResign(String executorId, String employeeId) {
		// ���ΧP�_executor�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		// ���ΧP�_�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee executor = dao.findById(executorId).get();
		if (!executor.getDepartment().equalsIgnoreCase("HR")) {
			// ���O�޲z����HR�������ϥΪ̡A�v������
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
		// ���ΧP�_employeeId�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(
					new ResignApplication(employeeId, employee.getDepartment(), LocalDate.now().plusMonths(1), "���n���F"));
		} catch (Exception e) {
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateInfo(String executorId, Employee employee) {
		// ���ΧP�_executor�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
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
		// ���ΧP�_employeeId�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
		return new EmployeeRes(RtnCode.SUCCESSFUL, dao.findById(employeeId).get());
	}

	@Override
	public EmployeeRes findStaffInfo(String callerId, String targetId) {
		// ���ΧP�_callerId�O�_���šA�]����k�����Ologin��~��ϥΡA�blogin��k���w���P�_
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
//		 1.�P�_�P�����Bcaller�O���D��;2.caller�O HR ����
//		 and�|����
		 if((callerDepartment.equals(targetInfo.getDepartment()) &&
				 JobPosition.hasReviewPermission(callerInfo.getJobPosition())) ||
				 callerDepartment.equalsIgnoreCase("HR"))  {
			 return new EmployeeRes(RtnCode.SUCCESSFUL,targetInfo);
		 }
		 return new EmployeeRes(RtnCode.PERMISSION_DENIED,null);
	}

}
