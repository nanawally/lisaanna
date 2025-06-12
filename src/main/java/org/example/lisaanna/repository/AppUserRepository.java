package org.example.lisaanna.repository;

import org.example.lisaanna.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfacet som har direktkontakt med databasen. Innehåller en "inbyggd" method som följer
 * konventionen "find" + "By" + {entitetsattribut}.
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
