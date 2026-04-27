package com.aceprep.agent;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.aceprep.rag.AdvancedRagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicAgentOrchestrator {

    private final ChatClient chatClient;
    private final AdvancedRagService ragService;

    public AgentResponse execute(String query, Long studentId,
                                 String sessionId, String subjectCode) {

        List<String> trace = new ArrayList<>();

        // STEP 1: Retrieve documents
        var docs = ragService.retrieve(query, subjectCode, studentId);
        trace.add("Retrieved " + docs.size() + " docs");

        // STEP 2: Generate answer
        String answer = chatClient.prompt()
                .user(query)
                .call()
                .content();

        trace.add("Generated answer");

        return AgentResponse.builder()
                .answer(answer)
                .executionTrace(trace)
                .iterationsUsed(1)
                .latencyMs(100)
                .build();
    }
}