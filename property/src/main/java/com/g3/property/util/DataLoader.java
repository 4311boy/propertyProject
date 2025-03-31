package com.g3.property.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.entity.User;
import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

  private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  private ListingRepository listingRepository;
  private AgentRepository agentRepository;
  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;
  
  public DataLoader(ListingRepository listingRepository, AgentRepository agentRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.listingRepository = listingRepository;
    this.agentRepository = agentRepository;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostConstruct
  public void loadData() {
    // Clear the database first
    listingRepository.deleteAll();
    agentRepository.deleteAll();
    userRepository.deleteAll();

    logger.info("loading data");
    // Load data
    listingRepository.saveAll(List.of(
      Listing.builder().streetName("ANG MO KIO AVE 10").price(232000L).town("ANG MO KIO").build(),
      Listing.builder().streetName("YISHUN RING ROAD AVE 10").price(300000L).town("YISHUN").build(),
      Listing.builder().streetName("BEDOK ROAD AVE 10").price(600000L).town("BEDOK").build()
    ));
 
        
    agentRepository.saveAll(List.of(
      Agent.builder().firstName("Agent").lastName("Admin").email("agentadmin@mail.com").password(passwordEncoder.encode("password")).role("ADMIN").build(),
      Agent.builder().firstName("John").lastName("Tan").email("johntan@mail.com").password(passwordEncoder.encode("password")).role("AGENT").build(),
      Agent.builder().firstName("Alison").lastName("Lee").email("alisonlee@mail.com").password(passwordEncoder.encode("password")).role("AGENT").build(),
      Agent.builder().firstName("Tony").lastName("Baloney").email("tonybaloney@mail.com").password(passwordEncoder.encode("password")).role("AGENT").build()
    ));

    userRepository.saveAll(List.of(
      User.builder().firstName("User").lastName("Admin").email("useradmin@mail.com").password(passwordEncoder.encode("password")).role("ADMIN").phoneNumber("123456").build(),
      User.builder().firstName("Dave").lastName("Lim").email("davelim@mail.com").password(passwordEncoder.encode("password")).role("USER").phoneNumber("123456").build(),
      User.builder().firstName("Janey").lastName("Lee").email("janeylee@mail.com").password(passwordEncoder.encode("password")).role("USER").phoneNumber("123456").build(),
      User.builder().firstName("Steve").lastName("Rogers").email("steverogers@mail.com").password(passwordEncoder.encode("password")).role("USER").phoneNumber("123456").build()
    ));

  }
}
