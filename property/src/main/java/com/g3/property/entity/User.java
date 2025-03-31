package com.g3.property.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements ApplicationUserInterface{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, name = "email")
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is a required field.")
    @ColumnTransformer(write = "LOWER(?)")
    private String email;

    @Column(nullable = false, name = "first_name")
    @NotBlank(message = "First name is a required field.")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    @NotBlank(message = "Last name is a required field.")
    private String lastName;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(name="role")
    private String role;

    @NotBlank(message="Password is a required field")
    @Column(name="password")
    private String password;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
        name = "user_favourites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "listing_id")
    )
    private List<Listing> favouriteListings;

    public void addFavourite(Listing listing) {
        this.favouriteListings.add(listing);
    }

}
