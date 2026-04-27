package com.aceprep.dto;

import java.util.Map;

public record AgentToolRequest(
        String tool,
        Map<String, Object> args
) {}