package com.example.attendance;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeDao;

@SpringBootTest
public class EmployeeTest {
	
	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;

	//¹w³] :10¤ÀÄÁ
//	@Value("${authcode.expired.time :10}")
//	private int authCodeExpiredTime;
	
	@Autowired
	private EmployeeDao employeeDao;

	@Test
	public void createAdminTest() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Employee employee = employeeDao.save(new Employee("Admin", "ADMIN", "Asmin",
				encoder.encode("$ADMiN^_^Otp"),"admin@G", true, "99", LocalDate.now(),
				LocalDate.now()));
		Assert.isTrue(employee != null, "Create admin erroe");
	}
	
	@Test
	public void PwTest() {
		System.out.println(authCodeExpiredTime);
	}
}
