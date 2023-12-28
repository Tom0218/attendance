package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.entity.LeaveType;

public interface LeaveTypeDao extends JpaRepository<LeaveType, Integer>{

}
