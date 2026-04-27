package com.aceprep.agent;

import java.util.Map;

public record ToolCall(
        String tool,
        Map<String, Object> args
) {}