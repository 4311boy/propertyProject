package com.g3.property.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.ListingRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // convertion of Java object to JSON

    @Autowired
    private ListingRepository listingRepository;
    
    @Autowired
    private AgentRepository agentRepository;

    private Agent testAgent;
    private Listing testListing;

    @BeforeEach
    public void setup() {

        // Use an existing agent from your data loader
        testAgent = agentRepository.findByEmail("johntan@mail.com")
                                .orElseThrow(() -> new RuntimeException("Test agent not found"));
        
        // Create test listing linked to the agent
        Listing listing = Listing.builder()
                .town("Tampines")
                .streetName("Tampines Street 45")
                .price(1200000L)
                .agent(testAgent) // Link to agent
                .build();
               
        testListing = listingRepository.save(listing);
   
    }

    @DisplayName("Listing creation SIT test")
    @Test
    public void createListingTest() throws Exception {

        // Create a Listing object
        Listing savedListing = Listing.builder()
                                    .town("Yishun")
                                    .streetName("Yishun Ring Road 10")
                                    .price(1000000L)
                                    .build();


        String newListingAsJSON = objectMapper.writeValueAsString(savedListing);
    
        // Build the request
        // using the test agent to create the listing 
        RequestBuilder requestListing = MockMvcRequestBuilders.post("/listings/agent/" + testAgent.getId())
            .with(SecurityMockMvcRequestPostProcessors.httpBasic(testAgent.getEmail(), "password"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(newListingAsJSON);

          
        // Perform the request, get response and assert
        mockMvc.perform(requestListing)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.town").value("Yishun"))
                .andExpect(jsonPath("$.streetName").value("Yishun Ring Road 10"))
                .andExpect(jsonPath("$.price").value(1000000L));
    }

    @Test
    @DisplayName("Update listing SIT test")
    public void updateListingTest() throws Exception {
        Listing updatedListing = Listing.builder()
                .town("Bedok")
                .streetName("Bedok North Avenue 3")
                .price(1500000L)
                .build();

        String updatedListingAsJSON = objectMapper.writeValueAsString(updatedListing);
        
        RequestBuilder requestListing = MockMvcRequestBuilders.put("/listings/agent/" + testAgent.getId() + "/" + testListing.getId())   
            .with(SecurityMockMvcRequestPostProcessors.httpBasic(testAgent.getEmail(), "password"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedListingAsJSON);

        mockMvc.perform(requestListing)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.town").value("Bedok"));
    }


    @Test
    @DisplayName("Authentication fails with wrong password")
    public void authFailsWithWrongPassword() throws Exception {
        // Create a Listing object
        Listing newListing = Listing.builder()
                .town("Yishun")
                .streetName("Yishun Ring Road 10")
                .price(1000000L)
                .build();

        String newListingAsJSON = objectMapper.writeValueAsString(newListing);

        // Use the correct email but WRONG password
        RequestBuilder requestListing = MockMvcRequestBuilders.post("/listings/agent/" + testAgent.getId())
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("johntan@mail.com", "wrongpassword"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(newListingAsJSON);
        
        // Expect 401 Unauthorized
        mockMvc.perform(requestListing)
                .andExpect(status().isUnauthorized());
    }
    

}
