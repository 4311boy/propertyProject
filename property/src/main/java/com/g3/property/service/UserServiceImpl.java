package com.g3.property.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.g3.property.entity.Listing;
import com.g3.property.entity.User;
import com.g3.property.exception.ListingNotFoundException;
import com.g3.property.exception.UserNotFoundException;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private ListingRepository listingRepository;


    public UserServiceImpl(UserRepository userRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }


    @Override
    public User createUser(User user) {

        //encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        logger.info("Creating new user record");
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        logger.info("Retrieving all user records");
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        logger.info("Retrieving details for user id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        User userToUdpate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userToUdpate.setEmail(user.getEmail());
        userToUdpate.setFirstName(user.getFirstName());
        userToUdpate.setPhoneNumber(user.getPhoneNumber());
        userToUdpate.setLastName(user.getLastName());
        return userRepository.save(userToUdpate);
    }

    @Override
    public void deleteUser(Long id) {
       logger.info("Deleting user with id: {}", id);
       userRepository.deleteById(id);
    }

    @Override
    public void updateUserFavouriteListing(Long userId, Long listingId) {
        logger.info("Updating user with id: {} favourite listing with id: {}", userId, listingId);

        //find the listing by listing id 
        Listing listingToAdd = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("listing not found"));
        
        //find user by userID
        User selectUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        
        //add listing to user
        selectUser.addFavourite(listingToAdd);
        userRepository.save(selectUser);
    }

    @Override
    public List<Listing> getUserFavouriteListing(Long userId) {
        logger.info("Getting user with id: {} favourite listing", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFavouriteListings();
    }
    
}
