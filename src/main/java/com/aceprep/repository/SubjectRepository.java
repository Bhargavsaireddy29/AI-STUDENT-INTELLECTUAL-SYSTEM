package com.aceprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aceprep.model.Subject;

//SubjectRepository
public interface SubjectRepository extends JpaRepository<Subject, Long> {}