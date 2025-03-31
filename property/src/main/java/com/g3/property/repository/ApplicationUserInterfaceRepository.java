package com.g3.property.repository;
import java.util.Optional;
import com.g3.property.entity.ApplicationUserInterface;
public interface ApplicationUserInterfaceRepository {
    Optional<? extends ApplicationUserInterface> findByEmail(String email);
}
