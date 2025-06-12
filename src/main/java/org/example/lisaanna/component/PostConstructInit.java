package org.example.lisaanna.component;

import jakarta.annotation.PostConstruct;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 Den här klassen kör kod direkt efter att Spring har startats upp
 */
@Component
public class PostConstructInit {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public PostConstructInit(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Annotationen PostConstruct gör att metoden körs en (1) gång omedelbart efter att beanen
     * är skapad och dependencies är injicerade - dvs efter att applikationen startats. Metoden
     * skapar en AppUser som direkt läggs in i databasen.
     */
    @PostConstruct
    public void init(){
        if (appUserRepository.findByUsername("user") == null) {
            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ADMIN");
            appUserRepository.save(user);
        }
    }
}
