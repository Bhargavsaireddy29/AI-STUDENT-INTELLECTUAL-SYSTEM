package com.aceprep.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

    public double calculateAttendance(Long studentId) {
        return 80.0;
    }
}