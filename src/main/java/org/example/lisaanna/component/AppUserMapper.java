package org.example.lisaanna.component;

import org.example.lisaanna.config.SecurityConfig;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.web.AppUserDTO;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    private SecurityConfig securityConfig;

    public AppUserMapper(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    public AppUser toAppUser(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setConsentGiven(dto.isConsentGiven());
        return user;
    }

    public AppUserDTO toAppUserDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        dto.setRole(user.getRole());
        dto.setConsentGiven(user.isConsentGiven());
        return dto;
    }

}
