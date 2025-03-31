package com.g3.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class AgentRegistrationRequest {
    private String firstName; 
    private String lastName; 
    private String email; 
    private String password;
}
