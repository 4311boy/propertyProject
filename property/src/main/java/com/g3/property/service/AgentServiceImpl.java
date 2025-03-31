package com.g3.property.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.g3.property.dto.AgentRegistrationRequest;
import com.g3.property.entity.Agent;
import com.g3.property.exception.AgentAlreadyExistsException;
import com.g3.property.exception.AgentNotFoundException;
import com.g3.property.repository.AgentRepository;
@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AgentRepository agentRepository;
    private AgentAuthorizationService agentAuthService;

    public AgentServiceImpl(AgentRepository agentRepository, AgentAuthorizationService agentAuthService) {
        this.agentAuthService = agentAuthService;
        this.agentRepository = agentRepository;
    }

    // create agent
    // NOTE: This is only for testing
    @Override
    public Agent createAgent(Agent agent) {
        logger.info("Creating new agent record");
        return agentRepository.save(agent);
    }


    @Override
    public List<Agent> getAllAgent() {
        logger.info("Retrieving all agent records");
        return agentRepository.findAll();
    }

    // register agent
    @Override
    public Agent registerAgent(AgentRegistrationRequest request) {
        logger.info("Registering new agent");
  
        String email = request.getEmail().toLowerCase();

        // Check if an agent with this email already exists
        if (agentRepository.existsByEmail(email)) {
            logger.warn("Agent with email {} already exists", email);
            throw new AgentAlreadyExistsException(email);
        }

        Agent agent = Agent.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail().toLowerCase())
                        // Encode the password before saving
                        .password(passwordEncoder.encode(request.getPassword()))
                        // Set default role to AGENT
                        .role("AGENT")
                        .build();
        
        logger.info("Agent registration successful for email: {}", email);
        return agentRepository.save(agent);
    }

    // get one agent by id
    @Override
    public Agent getAgent(Long id) {
        logger.info("Retrieving details for agent id: {}", id);
        Agent foundAgent = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + id));
        
        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(foundAgent.getId());
        
        return foundAgent;
    }

     // get one agent by email
     @Override
     public Agent getAgentbyEmail(String email) {

        logger.info("Retrieving details for agent email: {}", email);
        Agent foundAgent = agentRepository.findByEmail(email).orElseThrow(() 
                                -> new AgentNotFoundException("cant find agent with email " + email));

        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(foundAgent.getId());
        
        return foundAgent;
     }

    // update agent
    @Override
    public Agent updateAgent(Long id, Agent updatedFields) {
 
        Agent agentToUpdate = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + id));
       
        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(agentToUpdate.getId());

        logger.info("agentToUpdate: {}", agentToUpdate.getFirstName());
        logger.info("updatedFields: {}", updatedFields.getFirstName());
        
        Agent updatedAgent = Agent.builder().id(agentToUpdate.getId())
                                        .firstName(updatedFields.getFirstName() != null ? updatedFields.getFirstName() : agentToUpdate.getFirstName())
                                        .lastName(updatedFields.getLastName() != null ? updatedFields.getLastName() : agentToUpdate.getLastName())
                                        .email(updatedFields.getEmail() != null ? updatedFields.getEmail() : agentToUpdate.getEmail())
                                        .password(updatedFields.getPassword() != null ? passwordEncoder.encode(updatedFields.getPassword()) : passwordEncoder.encode(agentToUpdate.getPassword()))
                                        .createdAt(agentToUpdate.getCreatedAt())
                                        .role("AGENT")
                                        .build();
        logger.info("Updated details for agent id: {}", id);
        return agentRepository.save(updatedAgent);
    }

    // delete agent
    @Override
    public void deleteAgent(Long id) {
        logger.info("Deleted record for agent id: {}", id);
        
        Agent agentToDelete = agentRepository.findById(id).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + id));

        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(agentToDelete.getId());

        agentRepository.deleteById(agentToDelete.getId());
    }

}
