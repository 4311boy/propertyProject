package com.g3.property.service;

import java.util.List;

import com.g3.property.dto.AgentRegistrationRequest;
import com.g3.property.entity.Agent;
public interface AgentService {
    Agent createAgent(Agent agent);
    Agent getAgent(Long id);
    List <Agent> getAllAgent();
    Agent getAgentbyEmail(String email);
    Agent updateAgent(Long Id, Agent agent);
    void deleteAgent(Long id);

    // register agent
    Agent registerAgent(AgentRegistrationRequest request);
}
