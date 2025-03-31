package com.g3.property.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.g3.property.entity.User;
import com.g3.property.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Mock the UserRepository
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    // test to update user
    @Test
    public void testUpdateUser() {

        // create user first before updating
        User user = User.builder().firstName("Happy").lastName("Hogan")
                        .phoneNumber("90192837").role("USER").password("password")
                        .email("happyhogan@mail.com").build();
        Long userId = 1L;

        // expected updated user details
        User updatedUser = User.builder().firstName("Happy").lastName("Hogan")
                            .role("USER").password("password")
                            .phoneNumber("90109010").email("happyhogan2@mail.com").build();

        // mock repo methods
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // update user in the repository 
        userService.updateUser(userId, updatedUser);

        User retrievedUser = userRepository.findById(userId).get();

        // check if the results are expected
        assertEquals(updatedUser.getPhoneNumber(), retrievedUser.getPhoneNumber());
        assertEquals(updatedUser.getEmail(), retrievedUser.getEmail());

    }
    
}
