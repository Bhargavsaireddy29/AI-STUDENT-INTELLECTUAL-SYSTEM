package com.aceprep.service;

import org.springframework.stereotype.Service;

import com.aceprep.repository.LlmUsageLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CostTrackingService {

    private final LlmUsageLogRepository repository;

    public void recordUsage(Long studentId, String operation,
                            String model, int inputTokens, int outputTokens) {

        // Future: persist into DB
        System.out.println("Tracking tokens: " + inputTokens);
    }
}