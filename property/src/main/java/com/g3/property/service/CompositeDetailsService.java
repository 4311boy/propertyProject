package com.g3.property.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;

import com.g3.property.entity.ApplicationUserInterface;
import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.UserRepository;

import java.util.Optional;

// going to be use within configuration
// and convert into beans
public class CompositeDetailsService implements UserDetailsService {

    private AgentRepository agentRepository;
    private UserRepository userRepository;

    public CompositeDetailsService(AgentRepository agentRepository, UserRepository userRepository) {
        this.agentRepository = agentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // Convert your Agent entity already implements ApplicationUserInterface
        // and we are using ApplicationUserInterface in security agent object
       
        Optional<? extends ApplicationUserInterface> appUserOpt = agentRepository.findByEmail(email);

        //checl all email in user repository
        if (!appUserOpt.isPresent()) {
            appUserOpt = userRepository.findByEmail(email);
        }

        // throw exception if not found
        ApplicationUserInterface appUser = appUserOpt.orElseThrow(() -> new UsernameNotFoundException("User or agent email not found"));

        // Convert the ApplicationUserInterface into a 
        // Spring Security UserDetails object.
        return User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword()) // encoded password
                    .roles(appUser.getRole())
                    .build();

    }
}

