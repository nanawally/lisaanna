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
 *
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
     * @param username
     * @return
     * @throws UsernameNotFoundException
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
