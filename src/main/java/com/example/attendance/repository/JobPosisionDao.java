package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.JobPosision;

@Repository
public interface JobPosisionDao extends JpaRepository<JobPosision, String>{

}
