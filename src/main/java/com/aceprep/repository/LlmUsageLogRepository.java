package com.aceprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aceprep.model.LlmUsageLog;

//LlmUsageLogRepository
public interface LlmUsageLogRepository extends JpaRepository<LlmUsageLog, Long> {}