package com.example.attendance.repository;

import java.time.LocalDate;

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

//	clearAutomatically = true:清除持久化上下文;即清除暫存資料

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active =:inputActive where id = :inputId") // @Query回傳的格式為int
	public int updateActivate(@Param("inputId") String employeeId, @Param("inputActive") boolean active);

	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE Employee AS e SET "
			+ " department  = CASE WHEN :inputDepartment  IS NULL THEN e.department   ELSE :inputDepartment END,"
			+ " name        = CASE WHEN :inputName        IS NULL THEN e.name         ELSE :inputName END,"
			+ " email       = CASE WHEN :inputEmail       IS NULL THEN e.email        ELSE :inputEmail END,"
			+ " jobPosition = CASE WHEN :inputJobPosition IS NULL THEN e.jobPosition  ELSE :inputJobPosition END,"
			+ " birthDate   = CASE WHEN :inputBirthDate   IS NULL THEN e.birthDate    ELSE :inputBirthDate END,"
			+ " arrivalDate = CASE WHEN :inputArrivalDate IS NULL THEN e.arrivalDate  ELSE :inputArrivalDate END,"
			+ " annualLeave = CASE WHEN :inputAnnualLeave = 0     THEN  e.annualLeave  ELSE :inputAnnualLeave END,"
			+ " sickLeave   = CASE WHEN :inputSickLeave   = 0     THEN  e.sickLeave    ELSE :inputSickLeave END"
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
//	@Query(value = "update Employee set  =:inputActive where id = :inputId")
//	public int updatePassword(@Param("inputId") String id, @Param("inputPwd") String newPwd);
};
