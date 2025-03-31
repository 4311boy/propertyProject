package com.g3.property.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/users/**", "/listing/**", "/agent/**"))
                .authorizeHttpRequests(auth -> auth
                        // endpoints for /users
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN") // Only Admin can view the full list of users
                        .requestMatchers(HttpMethod.POST, "/users").permitAll() // Anyone can register
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "USER") // User or Admin can view
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasAnyRole("ADMIN", "USER")  // User or Admin can delete
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN", "USER")  // User or Admin can edit
                        .requestMatchers(HttpMethod.GET, "/users/{userId}/favourites").hasAnyRole("ADMIN", "USER")  // User or Admin can view favourites of specific user
                        .requestMatchers(HttpMethod.POST, "/users/{userId}/favourites/{listingId}").hasRole("USER") // Only User can add to his/her favourites
                        
                        // endpoints for /listing
                        .requestMatchers(HttpMethod.POST, "listing/agent/{agentId}").hasRole("AGENT")  // To change the role
                        .requestMatchers(HttpMethod.GET, "listing/search").authenticated() //To change role
                        .requestMatchers(HttpMethod.GET, "listing/agent/{id}").hasRole("AGENT") //To change role
                        .requestMatchers(HttpMethod.PUT, "listing/agent/{agentId}/{listingId").hasAnyRole("AGENT", "ADMIN") //To change role
                        .requestMatchers(HttpMethod.DELETE, "listing/agent/{agentId}/{listingId}").hasAnyRole("ADMIN", "AGENT") //To change role

                        //endpoints for /agent
                        .requestMatchers(HttpMethod.GET, "/agent/{id}").hasAnyRole("ADMIN", "AGENT") // Agent or Admin can view
                        .requestMatchers(HttpMethod.POST, "/agent").hasAnyRole("ADMIN", "AGENT") // Agent or Admin can register
                        .requestMatchers(HttpMethod.PUT, "agent/{id}").hasAnyRole("AGENT", "ADMIN") // Agent or Admin can edit
                        .requestMatchers(HttpMethod.DELETE, "agent/{id}").hasAnyRole("ADMIN", "AGENT") //Agent or Admin can delete

                        .anyRequest().permitAll())
                // .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails agent = User.builder()
                .username("agent")
                .password(passwordEncoder.encode("password"))
                .roles("AGENT")
                .build();
        return new InMemoryUserDetailsManager(admin, user, agent);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}