package com.aceprep.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDashboardDTO {
    private String riskLevel;
    private double attendance;
    private double averageMarks;
    private String recommendation;
}