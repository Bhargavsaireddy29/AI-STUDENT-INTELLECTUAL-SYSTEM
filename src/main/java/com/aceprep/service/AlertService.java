package com.aceprep.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlertService {

    public String sendAlert(Long studentId, String type, String message) {
        log.info("Sending alert to {} type {}", studentId, type);
        return "Alert sent";
    }
}