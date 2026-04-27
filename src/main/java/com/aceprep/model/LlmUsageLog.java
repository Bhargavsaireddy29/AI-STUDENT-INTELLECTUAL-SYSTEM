package com.aceprep.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "llm_usage_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private String operation;
    private String model;
    private int inputTokens;
    private int outputTokens;
}