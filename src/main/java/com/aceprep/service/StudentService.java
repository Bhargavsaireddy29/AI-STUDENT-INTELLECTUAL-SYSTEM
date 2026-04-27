package com.aceprep.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aceprep.dto.StudentDashboardDTO;
import com.aceprep.repository.ConversationRepository;
import com.aceprep.repository.MarksRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final MarksRepository marksRepository;
    private final ConversationRepository conversationRepository;

    public String getStudentContextSummary(Long studentId) {
        return "Student analytics summary for ID: " + studentId;
    }

    public Map<String, Object> getStudentAnalytics(Long studentId, String metric) {
        Map<String, Object> data = new HashMap<>();
        data.put("studentId", studentId);
        data.put("metric", metric);
        return data;
    }

    public StudentDashboardDTO buildDashboard(Long studentId) {
        return StudentDashboardDTO.builder()
                .riskLevel("MEDIUM")
                .attendance(78)
                .averageMarks(72)
                .recommendation("Improve consistency")
                .build();
    }

    public Map<String, Object> generateAiReport(Long studentId, String type) {
        return Map.of(
                "studentId", studentId,
                "type", type,
                "report", "AI generated report"
        );
    }
}