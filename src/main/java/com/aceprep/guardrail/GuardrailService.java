package com.aceprep.guardrail;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GuardrailService {

    private static final List<String> BLOCKED = List.of(
            "ignore instructions",
            "jailbreak",
            "act as"
    );

    public String sanitizeInput(String input) {

        String lower = input.toLowerCase();

        for (String keyword : BLOCKED) {
            if (lower.contains(keyword)) {
                log.warn("Prompt injection attempt detected");
                return "[BLOCKED]";
            }
        }

        return input;
    }

    public String validateOutput(String output) {
        if (output == null || output.isBlank()) {
            return "No response generated";
        }

        if (output.length() > 5000) {
            return output.substring(0, 5000);
        }

        return output;
    }
}