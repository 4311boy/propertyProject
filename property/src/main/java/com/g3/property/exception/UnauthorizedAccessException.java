package com.g3.property.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(Long agentId) {
        super("Listing does not belong to the agent with id " + agentId);
    }
}
