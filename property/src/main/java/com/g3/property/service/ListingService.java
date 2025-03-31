package com.g3.property.service;

import com.g3.property.entity.Listing;
import java.util.List;

public interface ListingService {
    Listing createListing(Listing listing ,Long agentId);

    List<Listing> searchListings(String town, Double minPrice, Double maxPrice);

    List<Listing> getAgentListing(Long agentId);

    Listing updateListing(Long listingId, Listing listing, Long agentId);

    void deleteListing(Long listingId, Long agentId);
}
