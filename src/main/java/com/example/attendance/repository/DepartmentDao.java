package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Department;

@Repository
public interface DepartmentDao extends JpaRepository< Department , String> {

	public boolean existsByIdIn(List<String> ids);
	
	public boolean existsByName(String name);
}
