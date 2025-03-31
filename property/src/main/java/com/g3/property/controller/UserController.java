package com.g3.property.controller;

import org.springframework.web.bind.annotation.RestController;

import com.g3.property.entity.Listing;
import com.g3.property.entity.User;
import com.g3.property.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;


    public UserController(UserService customerService) {
        this.userService = customerService;
    }
    
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {    
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/favourites/{listingId}")
    public ResponseEntity<HttpStatus> saveFavorite(@PathVariable Long userId, @PathVariable Long listingId) {
        userService.updateUserFavouriteListing(userId, listingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<Listing>> getUserFavorites(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserFavouriteListing(userId), HttpStatus.OK);
    }
    
}
