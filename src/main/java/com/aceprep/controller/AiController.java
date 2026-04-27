package com.aceprep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aceprep.agent.AcademicAgentOrchestrator;
import com.aceprep.agent.AgentResponse;
import com.aceprep.dto.ChatRequest;
import com.aceprep.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AcademicAgentOrchestrator orchestrator;

    @PostMapping("/chat")
    public ResponseEntity<AgentResponse> chat(
            @RequestBody ChatRequest req,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                orchestrator.execute(
                        req.query(),
                        user.getId(),
                        req.sessionId(),
                        req.subjectCode()
                )
        );
    }
}