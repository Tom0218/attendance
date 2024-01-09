package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {

	public Employee findByEmail(String email);

	public List<Employee> findAllByIdContainingAndNameAndDepartment(String id, String name, String department);
	
	//% 是一个通配符，通常与 LIKE 运算符一起使用，用于模糊匹配字符串。它表示匹配零个或多个任意字符。%something 表示以 "something" 结尾的字符串。
	//something% 表示以 "something" 开头的字符串。
	//%something% 表示包含 "something" 的字符串，可以在字符串的任意位置。
	//冒号 : 通常用作参数的标识符。当你看到类似 :id、:name、:department 这样的写法时，它表示这是一个查询参数
	//如果 :id 是 "123"，那么 e.id LIKE %:id% 将匹配任何包含 "123" 的 id 字段的记录。
	@Query("SELECT e FROM Employee e WHERE" +
	        " e.id LIKE %:id%" +
	        " AND e.name LIKE %:name%" +
	        " AND e.department LIKE %:department%")
	public List<Employee> findEmployee(
			@Param("id") String id,
			@Param("name")String name,
			@Param("department")String department);

	@Modifying(clearAutomatically = true)
	@Query(value = "SELECT e FROM Employee e WHERE e.department = :inputDepartment AND job_Position = 'Supervisor'")
	public List<Employee> findSupervisor(@Param("inputDepartment") String department);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active = :inputAction where id = :inputId")
	public int updateActivate(@Param("inputId") String employeeId, @Param("inputAction") boolean active);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee as e set "
			+ " department  = case when :inputDepartment  is null then e.department   else :inputDepartment end,"
			+ " name        = case when :inputName        is null then e.name         else :inputName end,"
			+ " email       = case when :inputEmail       is null then e.email        else :inputEmail end,"
			+ " jobPosition = case when :inputJobPosition is null then e.jobPosition  else :inputJobPosition end,"
			+ " birthDate   = case when :inputBirthDate   is null then e.birthDate    else :inputBirthDate end,"
			+ " arrivalDate = case when :inputArrivalDate is null then e.arrivalDate  else :inputArrivalDate end,"
			+ " annualLeave = case when :inputAnnualLeave = 0    then e.annualLeave  else :inputAnnualLeave end,"
			+ " sickLeave   = case when :inputSickLeave   = 0    then e.sickLeave    else :inputSickLeave end"
			+ " where e.id = :inputId")
	public int updateInfo(@Param("inputId") String id, //
			@Param("inputDepartment") String department, //
			@Param("inputName") String name, //
			@Param("inputEmail") String email, //
			@Param("inputJobPosition") String jobPosition, //
			@Param("inputBirthDate") LocalDate birthDate, //
			@Param("inputArrivalDate") LocalDate arrivalDate, //
			@Param("inputAnnualLeave") int annualLeave, //
			@Param("inputSickLeave") int sickLeave);

//	@Modifying(clearAutomatically = true)
//	@Transactional
//	@Query(value = "UPDATE Employee as e SET "
//			+ " department 	  	= COALESCE ( :inputDepartment, e.department), "
//			+ " name			= COALESCE  ( :inputName, e.name), "
//			+ " email 			= COALESCE  ( :inputEmail, e.email), "
//			+ " jobPosition 	= COALESCE  ( :inputJobPosition, e.jobPosition), "
//			+ " birthDate 		= COALESCE  ( :inputBirthDate , e.birthDate), "
//			+ " arrivalDate 	= COALESCE  ( :inputArrivalDate, e.arrivalDate), "
//			+ " annualLeave 	= CASE WHEN :inputAnnualLeave 	= 0 	THEN e.annualLeave ELSE :inputAnnualLeave END, "
//			+ " sickLeave 		= CASE WHEN :inputSickLeave 	= 0	 	THEN e.sickLeave ELSE :inputSickLeave END "
//			+ " WHERE e.id = :inputId")
//	public int updateInfo1(@Param("inputId") String id, //
//			@Param("inputDepartment") String department, //
//			@Param("inputName") String name, //
//			@Param("inputEmail") String email, //
//			@Param("inputJobPosition") String jobPosition, //
//			@Param("inputBirthDate") LocalDate birthDate, //
//			@Param("inputArrivalDate") LocalDate arrivalDate, //
//			@Param("inputAnnualLeave") int annualLeave, //
//			@Param("inputSickLeave") int sickLeave);

//	@Modifying(clearAutomatically = true)
//	@Transactional
//	@Query(value = "update Employee set pwd = :inputpwd where id = :inputId")
//	public int updatePassword(@Param("input") String employeeId, @Param("inputpwd") String newpwd);
}
