package com.g3.property.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.g3.property.dto.AgentRegistrationRequest;
import com.g3.property.entity.Agent;
import com.g3.property.exception.AgentNotFoundException;
import com.g3.property.repository.AgentRepository;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @InjectMocks
    private AgentServiceImpl agentService;  // implementation


    // inject all the dependencies
    @Mock
    private AgentRepository agentRepository;  // mock the repository

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AgentAuthorizationService agentAuthService;  // Mock for agentAuthService

    private Agent testAgent;
    private AgentRegistrationRequest testAgentInput;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //result of mock
        //NOTE : needed to return a hashed password
        testAgent = Agent.builder()
                .id(1L)
                .firstName("Alan")
                .lastName("Han")
                .password(passwordEncoder.encode("password")) 
                .role("AGENT")
                .email("alan.han@example.com")
                .build();

        // input of service layer
        testAgentInput = AgentRegistrationRequest.builder()
                        .firstName("Alan")
                        .lastName("Han")
                        .password("password")
                        .email("alan.han@example.com")
                        .build();
    }

    /** Test for Creating an Agent */
    @Test
    void testCreateAgent() {

        // Mock the save method
        when(agentRepository.save(any(Agent.class))).thenReturn(testAgent);

        //placing input in service layer 
        Agent createdAgent = agentService.registerAgent(testAgentInput);

        assertNotNull(createdAgent);
        assertEquals(testAgent.getFirstName(), createdAgent.getFirstName(), "The agent in repo should be the same as the agent created by service.");
        assertEquals(testAgent.getLastName(), createdAgent.getLastName());
        assertEquals(testAgent.getEmail(), createdAgent.getEmail());

        verify(agentRepository, times(1)).save(any(Agent.class));
    }

    /** Test for Finding an Agent by ID */
    @Test
    void testGetAgentById() {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(testAgent));

        Agent foundAgent = agentService.getAgent(1L);

        assertNotNull(foundAgent);
        assertEquals(1L, foundAgent.getId());
        assertEquals("Alan", foundAgent.getFirstName());

        verify(agentRepository, times(1)).findById(1L); // ✅ Ensure findById() was called once
    }
    
    /** Test for Finding an Agent by Email */
    @Test
    void testGetAgentByEmail() {
        when(agentRepository.findByEmail(testAgent.getEmail())).thenReturn(Optional.of(testAgent));
    
        Agent foundAgent = agentService.getAgentbyEmail(testAgent.getEmail());
    
        assertNotNull(foundAgent);
        assertEquals(testAgent.getId(), foundAgent.getId());
        assertEquals(testAgent.getEmail(), foundAgent.getEmail());
        assertEquals(testAgent.getFirstName(), foundAgent.getFirstName());
    
        verify(agentRepository, times(1)).findByEmail(testAgent.getEmail()); // ✅ Ensure findById() was called once
    }

    /** Test for Finding a Non-Existing Agent */
    @Test
    public void testGetAgentById_NotFound() {
        Long agentId = 999L; // Non-existing agent ID
        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(AgentNotFoundException.class, () -> {
            agentService.getAgent(agentId); // This should throw AgentNotFoundException
        });
    }
}