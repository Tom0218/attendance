package com.example.attendance;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeDao;

@SpringBootTest
public class EmployeeTest {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private EmployeeDao employeeDao;

	@Test
	public void createAdnimTest() {
		Employee employee = employeeDao.save(new Employee(
				"Admin", 
				"ADMIN", 
				"admin", 
				encoder.encode("$ADMIN^_^Otp"),
				"admin@G",
				"99",
				LocalDate.now(),
				LocalDate.now(),
				true));
		Assert.isTrue(employee != null, "Create Admin error");
	}

}
