package com.aceprep.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

    public String generateReport(Long studentId) {
        log.info("Generating report for {}", studentId);
        return "Generated Report";
    }
}