package com.g3.property.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g3.property.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    boolean existsByEmail(String email);
    
    Optional<Agent> findByEmail(String email);
    
}
