package com.g3.property.service;

import java.util.List;

import com.g3.property.entity.Listing;
import com.g3.property.entity.User;

public interface UserService {
    User createUser(User user);

    User getUser(Long id);

    List<User> getAllUser();

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    List<Listing> getUserFavouriteListing(Long id);

    void updateUserFavouriteListing(Long userId, Long listingId);

}
