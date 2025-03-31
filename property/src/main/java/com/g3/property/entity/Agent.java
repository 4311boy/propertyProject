package com.g3.property.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agent")
public class Agent implements ApplicationUserInterface{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="First name is a required field")
    @Column(nullable=false, name="first_name")
    private String firstName;

    @NotBlank(message="Last name is a required field")
    @Column(nullable=false, name="last_name")
    private String lastName;
    
    @Email(message = "Email should be valid.")
    @NotBlank(message="Email is a required field")
    @Column(nullable=false, unique=true, name="email")
    @ColumnTransformer(write = "LOWER(?)") //case insensitive
    private String email;

    @Column(name="role")
    private String role;

    @NotBlank(message="Password is a required field")
    @Column(name="password")
    private String password;
    
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    
}
