package com.g3.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g3.property.entity.Listing;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {

        // Find listings by town and price
        List<Listing> findByTownAndPriceBetween(String town, Double minPrice, Double maxPrice);
    
        // Find listings by town only
        List<Listing> findByTown(String town);
       
        // Find by price range only
        List<Listing> findByPriceBetween(Double minPrice, Double maxPrice);

        // Find listings by agent id
        List<Listing> findByAgentId(Long agentId);
    }
