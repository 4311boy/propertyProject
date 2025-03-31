package com.g3.property.service;

// import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.g3.property.entity.Agent;
import com.g3.property.exception.AccessDeniedException;
import com.g3.property.repository.AgentRepository;

@Service
public class AgentAuthorizationService {

    private AgentRepository agentRepository;

    public AgentAuthorizationService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Agent verifyAgentAccess(Long agentId) {
        // Retrieve the authenticated agent's email from the security context
        // and not directly from the database
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Load the authenticated agent from the database using the email
        Agent authenticatedAgent = agentRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Agent not found"));

        // Check if the authenticated agent's id matches the agentId provided in the URL
        if (!authenticatedAgent.getId().equals(agentId)) {
            throw new AccessDeniedException("You are not allowed to perform action on other accounts");
        }

        return authenticatedAgent;
    }
}
