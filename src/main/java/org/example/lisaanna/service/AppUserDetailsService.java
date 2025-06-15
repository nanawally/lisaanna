package org.example.lisaanna.service;

import org.example.lisaanna.component.LoggingComponent;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security letar automatiskt efter en UserDetailsService, som implementeras i denna klassen,
 * när den behöver autentisera användare. Eftersom klassen är annoterad med @Service kommer Spring
 * att hitta den när den letar efter Beans.
 *
 * UserDetailsService används automatiskt av Spring Security implicit, därför behöver man inte
 * implementera klassinnehållet någon annanstans i kodbasen.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;
    private LoggingComponent loggingComponent;

    public AppUserDetailsService(AppUserRepository appUserRepository, LoggingComponent loggingComponent) {
        this.appUserRepository = appUserRepository;
        this.loggingComponent = loggingComponent;
    }

    /**
     * @param username är användaren som behöver autentiseras
     * @return en autentiserad användare returneras, om inte ett
     * @throws UsernameNotFoundException kastas, när en användare inte hittas
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + appUser.getRole())
        );
        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                true, true, true, true,
                authorities
        );
    }
}
