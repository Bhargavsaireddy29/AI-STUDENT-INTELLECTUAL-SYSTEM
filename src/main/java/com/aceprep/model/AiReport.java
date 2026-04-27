package com.aceprep.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private String reportType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String riskLevel;
}