package com.g3.property.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name = "listing")
public class Listing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="Town is a required field")
    @Column(nullable= false, name="town")
    private String town;
    
    @NotBlank(message="Street name is a required field")
    @Column(nullable=false, name="street_name")
    private String streetName;

    @NotNull
    @Positive // if price must be greater than 0
    @Column(name="price")
    private Long price;
    
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    // Many-to-One relationship: Many listings can be created by one agent.
    @ManyToOne(optional = true) // allow listing to be not connected by agent
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = true)
    private Agent agent;
   
}
