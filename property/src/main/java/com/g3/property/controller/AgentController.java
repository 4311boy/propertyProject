package com.g3.property.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g3.property.dto.AgentRegistrationRequest;
import com.g3.property.entity.Agent;
import com.g3.property.service.AgentService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/agents")
public class AgentController {
    
    private AgentService agentService;
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    // //un-used
    // @PostMapping("/admin")
    // public ResponseEntity<Agent> createAgent(@Valid @RequestBody Agent agent) {
    //     return new ResponseEntity<>(agentService.createAgent(agent), HttpStatus.CREATED);
    // }

    @PostMapping("/register")
    public ResponseEntity<Agent> registerAgent(@Valid @RequestBody AgentRegistrationRequest agentRegistrationRequest) {
        return new ResponseEntity<>(agentService.registerAgent(agentRegistrationRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Agent>> getAllAgent() {
        return new ResponseEntity<>(agentService.getAllAgent(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgent(@PathVariable Long id) {
        return new ResponseEntity<>(agentService.getAgent(id), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Agent> getAgentByEmail(@RequestParam String email) {
        return new ResponseEntity<>(agentService.getAgentbyEmail(email), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(
        @PathVariable Long id, 
        @Valid @RequestBody Agent updatedFields
        ) {
        Agent updatedAgent = agentService.updateAgent(id, updatedFields);
        return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
}
