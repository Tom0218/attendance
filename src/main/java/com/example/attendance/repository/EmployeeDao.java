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

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active = :inputAction where id = :inputId")
	public int updateActivate(@Param("input") String employeeId, @Param("inputAction") boolean active);

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
