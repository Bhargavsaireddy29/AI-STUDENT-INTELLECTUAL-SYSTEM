package com.aceprep.agent;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentResponse {

    private String answer;
    private List<String> executionTrace;
    private int iterationsUsed;
    private int totalInputTokens;
    private int totalOutputTokens;
    private long latencyMs;
}