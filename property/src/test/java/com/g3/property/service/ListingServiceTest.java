package com.g3.property.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.AgentRepository;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    // private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @InjectMocks
    private ListingServiceImpl listingService;  // implementation

    // inject all the dependencies
    @Mock
    private ListingRepository listingRepository;  // mock the repository
    
    @Mock
    private AgentRepository agentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AgentAuthorizationService agentAuthService;  // Mock for agentAuthService

    private Listing testListing;
    private Agent testAgent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //result of mock
        //NOTE : needed to return a hashed password

        testAgent = Agent.builder()
                        .id(22L)
                        .firstName("Alan")
                        .lastName("Han")
                        .password(passwordEncoder.encode("password")) 
                        .role("AGENT")
                        .email("alan.han@example.com")
                        .build();

        testListing = Listing.builder()
                            .town("Yishun")
                            .streetName("Yishun Ring Road 10")
                            .price(1000000L)
                            .agent(testAgent)
                            .build();
    }

    /** Test for Creating an Agent */
    @Test
    void testCreateListing() {

        // mock finding through the agent repository

        // In createlisitng service, it verifies whether agents exists in repository 
        // before creating a new listing
        when(agentRepository.findById(testAgent.getId())).thenReturn(Optional.of(testAgent));

        // Mock the save method
        when(listingRepository.save(any(Listing.class))).thenReturn(testListing);

        //placing input in service layer 
        Listing createdListing = listingService.createListing(testListing, testAgent.getId());

        assertNotNull(createdListing);
        assertEquals(testListing.getTown(), createdListing.getTown(), "The listing in repo should be the same as the listing created by service.");
        assertEquals(testListing.getStreetName(), createdListing.getStreetName());
        assertEquals(testListing.getPrice(), createdListing.getPrice());

        verify(agentRepository, times(1)).findById(testAgent.getId());
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    
}