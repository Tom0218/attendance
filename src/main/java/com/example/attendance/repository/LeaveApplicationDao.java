package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.LeaveApplication;

@Repository
public interface LeaveApplicationDao extends JpaRepository<LeaveApplication, Integer>{
	
	public List<LeaveApplication> findByApplicationNo(String applicationNo);
	
	@Query("SELECT e FROM LeaveApplication e WHERE " + 
			" e.reviewerId LIKE %:inputReviewerId% " + 
			" AND e.applicationNo LIKE %:inputApplicationNo%" + 
			"AND e.employeeId LIKE %:inputEmployeeId%" )
	public List <LeaveApplication> findByReviewerId(@Param("inputReviewerId") String ReviewerId,
					@Param("inputApplicationNo") String applicationNo,
					@Param("inputEmployeeId")String employeeId);
	
}
