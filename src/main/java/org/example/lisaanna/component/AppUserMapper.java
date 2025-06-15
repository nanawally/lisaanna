package org.example.lisaanna.component;

import org.example.lisaanna.config.SecurityConfig;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.web.AppUserDTO;
import org.springframework.stereotype.Component;

/**
 Mappers ingår i servicelagret i en applikation. I denna klass konverterar vi vår Entity (AppUser)
 till sin DTO-motsvarighet, eller tvärtom, innan datan sparas eller bearbetas vidare.
 */
@Component
public class AppUserMapper {

    private SecurityConfig securityConfig;

    public AppUserMapper(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    /**
     * @param dto tas in. Den är en instans av AppUserDTO
     * @return en AppUser returneras
     */
    public AppUser toAppUser(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setConsentGiven(dto.isConsentGiven());
        return user;
    }

    /**
     * @param user tas in. Den är en instans av entiteten AppUser
     * @return en instans av AppUserDTO
     */
    public AppUserDTO toAppUserDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        dto.setRole(user.getRole());
        dto.setConsentGiven(user.isConsentGiven());
        return dto;
    }

}
