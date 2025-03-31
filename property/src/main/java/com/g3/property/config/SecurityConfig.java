package com.g3.property.config;

import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.UserRepository;
import com.g3.property.service.CompositeDetailsService;

@Configuration 
public class SecurityConfig {

    private AgentRepository agentRepository;
    private UserRepository userRepository; // Your user repository

    public SecurityConfig(AgentRepository agentRepository, UserRepository userRepository) {
        this.agentRepository = agentRepository;
        this.userRepository = userRepository;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> 
                auth

                // hasRole() method checks both authenticated and the authorizarion for the specified role
                // endpoints for /users
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN") // Only Admin can view the full list of users
                .requestMatchers(HttpMethod.POST, "/users").permitAll() // Anyone can register
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "USER") // User or Admin can view
                .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyRole("ADMIN", "USER")  // User or Admin can delete
                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN", "USER")  // User or Admin can edit
                .requestMatchers(HttpMethod.GET, "/users/{userId}/favourites").hasAnyRole("ADMIN", "USER")  // User or Admin can view favourites of specific user
                .requestMatchers(HttpMethod.POST, "/users/{userId}/favourites/{listingId}").hasRole("USER") // Only User can add to his/her favourites
                
                 //listing endpoints
                .requestMatchers(HttpMethod.POST, "/listings/agent/{agentId}").hasRole("AGENT")
                .requestMatchers(HttpMethod.GET, "/listings/agent/{agentId}").hasRole("AGENT")
                .requestMatchers(HttpMethod.PUT, "/listings/agent/{agentId}/{listingId}").hasRole("AGENT")
                .requestMatchers(HttpMethod.DELETE, "/listings/agent/{agentId}/{listingId}").hasRole("AGENT")

                //agent endpoints
                .requestMatchers(HttpMethod.GET, "/agents/email").hasRole("AGENT")
                .requestMatchers(HttpMethod.GET, "/agents/{id}").hasRole("AGENT")
                .requestMatchers(HttpMethod.PUT, "/agents/{id}").hasRole("AGENT")
                .requestMatchers(HttpMethod.DELETE, "/agents/{id}").hasRole("AGENT")
                .requestMatchers(HttpMethod.GET, "/agents").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/agents/admin").hasRole("ADMIN")

                .anyRequest().permitAll()  // All other endpoints require authentication
            );

            // HTTP Basic authentication
            http.httpBasic(Customizer.withDefaults());

            //Disable cross site request forgery
            http.csrf(csrf -> csrf.disable());


        return http.build();
    }

    // //InMemory Authentication
    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //     UserDetails admin = User.builder()
    //             .username("admin")
    //             .password(passwordEncoder.encode("password"))
    //             .roles("ADMIN")
    //             .build();
    //     UserDetails user = User.builder()
    //             .username("user")
    //             .password(passwordEncoder.encode("password"))
    //             .roles("USER")
    //             .build();
    //     return new InMemoryUserDetailsManager(admin, user);
    // }

    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    } 

    //Database Authentication
    @Bean
    public UserDetailsService userDetailsService() {

        //configure the CompositeDetailsService which implements UserDetailsService
        UserDetailsService userDetailsService = new CompositeDetailsService(agentRepository, userRepository);
        
        return userDetailsService;
    }

   
}
