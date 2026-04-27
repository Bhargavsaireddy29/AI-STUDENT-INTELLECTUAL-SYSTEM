package com.aceprep.dto;

public record ChatRequest(
        String query,
        String sessionId,
        String subjectCode
) {}