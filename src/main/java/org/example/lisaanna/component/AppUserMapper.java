package org.example.lisaanna.component;

import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.web.AppUserDTO;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {
    public AppUser toAppUser(AppUserDTO dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setConsentGiven(dto.isConsentGiven());
        return user;
    }

    public AppUserDTO toAppUserDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setConsentGiven(user.isConsentGiven());
        return dto;
    }

}
