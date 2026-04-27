package com.aceprep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aceprep.model.Conversation;

//ConversationRepository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {}
