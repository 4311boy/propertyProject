package com.g3.property.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.exception.AgentNotFoundException;
import com.g3.property.exception.ListingNotFoundException;
import com.g3.property.exception.UnauthorizedAccessException;
import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.ListingRepository;
@Service
public class ListingServiceImpl implements ListingService {

    private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

    private ListingRepository listingRepository;
    private AgentRepository agentRepository;
    private AgentAuthorizationService agentAuthService;

    public ListingServiceImpl(ListingRepository listingRepository, AgentRepository agentRepository, AgentAuthorizationService agentAuthService) {
        this.listingRepository = listingRepository;
        this.agentRepository = agentRepository;
        this.agentAuthService = agentAuthService;
    }

    @Override
    public Listing createListing(Listing listing, Long agentId) {
        logger.info("Creating new listing record for agent Id: {}", agentId);
        // make sure that the agent exists
        Agent agent = agentRepository.findById(agentId)
                                    .orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + agentId));

        // Verify that the authenticated agent matches the provided agentId
        // not allowed to create listing for other agents by using own credentials
        agentAuthService.verifyAgentAccess(agentId);
                                    
        listing.setAgent(agent); // set foreign key

        return listingRepository.save(listing);
    }

    @Override
    public List<Listing> searchListings(String town, Double minPrice, Double maxPrice) {

        //Declare empty listing
        List<Listing> foundListings = new ArrayList<>();

        // Check if town is not specified
        if (town == null || town.trim().isEmpty()) {
            foundListings = listingRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
        // Otherwise, search by town and price range
            String townCaps = town.toUpperCase(); // ensure search string is all uppercase
            foundListings = listingRepository.findByTownAndPriceBetween(townCaps, minPrice, maxPrice);
        }
        
        // .orElseThrow(() only works on List Optional
        if (foundListings.isEmpty()) {
            throw new ListingNotFoundException("No listings found in town: " + town + " with price range: " + minPrice + " - " + maxPrice);
        }
        logger.info("Found {} listings", foundListings.size());                                        
        return foundListings;
    }

    @Override
    public List<Listing> getAgentListing(Long agentId){
        logger.info("Getting all listings for agent Id: {}", agentId);

        // make sure that the agent exists
        agentRepository.findById(agentId).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + agentId));

        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(agentId);

        List<Listing> listingOfAgent = listingRepository.findByAgentId(agentId);

        // .orElseThrow(() only works on List Optional
        if (listingOfAgent.isEmpty()) {
            throw new ListingNotFoundException("No listings found for agent with id " + (agentId));
        }
        return listingOfAgent;
    }

    @Override
    public Listing updateListing(Long listingId, Listing listing, Long agentId) {
        logger.info("Updating listing with id {} for agent Id: {}", listingId,agentId);

        // make sure that the agent exists
        agentRepository.findById(agentId).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + agentId));

        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(agentId);
        
        // find whether listing exists 
        Listing listingToUpdate = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("Could not find listing with id: " + (listingId)));
        
        // Check if the listing's agent id matches the provided agent id
        if (!listingToUpdate.getAgent().getId().equals(agentId)) {
            throw new UnauthorizedAccessException(agentId);
        }

        // Update listing object
        listingToUpdate.setTown(listing.getTown());
        listingToUpdate.setStreetName(listing.getStreetName());
        listingToUpdate.setPrice(listing.getPrice());
   
        // Save the updated listing object to the DB
        Listing savedListing = listingRepository.save(listingToUpdate);
        return savedListing;
    }

    @Override
    public void deleteListing(Long listingId, Long agentId) {
        logger.info(null, "Deleting listing with id {} for agent Id: {}", listingId, agentId);
        // make sure that the agent exists
        agentRepository.findById(agentId).orElseThrow(() -> new AgentNotFoundException("cant find agent with id " + agentId));

        // Verify that the authenticated agent matches the provided agentId
        agentAuthService.verifyAgentAccess(agentId);
        
        // find whether listing exists 
        Listing listingToDelete = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("Could not find listing with id: " + (listingId)));
        
        // Check if the listing's agent id matches the provided agent id
        if (!listingToDelete.getAgent().getId().equals(agentId)) {
            throw new UnauthorizedAccessException(agentId);
        }

        // delete listing
        listingRepository.deleteById(listingId);
    }


}
