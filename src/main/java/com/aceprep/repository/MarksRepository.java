package com.aceprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aceprep.model.Marks;

//MarksRepository
public interface MarksRepository extends JpaRepository<Marks, Long> {}