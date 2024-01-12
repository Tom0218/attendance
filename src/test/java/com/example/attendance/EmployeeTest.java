package com.example.attendance;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.vo.EmployeeRes;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;

	@Autowired
	private EmployeeDao employeeDao;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	// ¹w³] :10¤ÀÄÁ
//	@Value("${authcode.expired.time :10}")
//	private int authCodeExpiredTime;

	@Test
	public void createAdminTest() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// String id, String department, String name, String pwd, String email, boolean
		// active,
		// String jobPosition, LocalDate birthDate, LocalDate arrivalDate
		Employee employee = employeeDao.save(new Employee("superadmin", "admin", "superadmin",
				encoder.encode("superadmin"), "superadmin", true, "superadmin", LocalDate.now(), LocalDate.now()));
		Assert.isTrue(employee != null, "Create admin erroe");
	}

	@Test
	public void searchTest() {
		// id; name; department
		List<Employee> employeeList = employeeDao.findAllByIdContainingAndNameAndDepartment("", "", "");
		List<Employee> employeeList2 = employeeDao.findEmployee("tom", "", "");
		if (employeeList.isEmpty()) {
			System.out.println("employee not found!");
		}
		for (Employee employee : employeeList) {
			System.out.println("employeeList :" + employee.getName());
		}
		for (Employee employee : employeeList2) {
			System.out.println("employeeList2 :" + employee.getName());
		}
	}
	
	@Test
	public void findSupervisorTest (){
		List<Employee> employeeList =  employeeDao.findSupervisor("IT");
		for(Employee employee:employeeList) {
			System.out.println(employee.getName());
		}
	}
	
	@Test
	public void test() {
		int x ;
		x = 0b111;
		System.out.println(x);
	}

}
