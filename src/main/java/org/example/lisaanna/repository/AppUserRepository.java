package org.example.lisaanna.repository;

import org.example.lisaanna.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

}
