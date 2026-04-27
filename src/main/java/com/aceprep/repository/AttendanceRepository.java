package com.aceprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aceprep.model.Attendance;

//AttendanceRepository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {}
