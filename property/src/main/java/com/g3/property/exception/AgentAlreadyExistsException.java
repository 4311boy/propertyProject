package com.g3.property.exception;

public class AgentAlreadyExistsException extends RuntimeException {
    public AgentAlreadyExistsException(String email) {
        super("Agent with email " + email + " already exists.");
    } 
}
