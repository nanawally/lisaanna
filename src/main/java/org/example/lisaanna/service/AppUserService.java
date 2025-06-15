package org.example.lisaanna.service;

import org.example.lisaanna.component.AppUserMapper;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.exception.UserNotFoundException;
import org.example.lisaanna.repository.AppUserRepository;
import org.example.lisaanna.web.AppUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Den här klassen interagerar med databaslagret, mappar DTOn till entiteter, och sköter viss
 * loggning. Majoriteten av klassen består av metoder som motsvarar CRUD-operationer.
 */
@Service
public class AppUserService {

    private final AppUserMapper appUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    /**
     * Kör loggning för debugging och liknande
     */
    public void run() {
        logger.info("Starting logger");
        try {
            logger.debug("Running logic...");
        } catch (Exception e) {
            logger.error("Error occurred", e);
        }
    }

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUserService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    /**
     * @return en lista över alla objekt i databasen
     */
    public List<AppUser> getAppUserList() {
        return appUserRepository.findAll();
    }

    /**
     * @param username användarnamnet som tillhör databasobjektet som ska hittas
     * @return användaren i databasen som matchar användarnamnet
     */
    public AppUser getAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    /**
     * @param appUserDTO DTO-versionen av användarna i databasen. Användare som valideras genom DTOn
     * sparas sedan i databasen.
     */
    public void saveUser(AppUserDTO appUserDTO) {
        AppUser appUser = appUserMapper.toAppUser(appUserDTO);
        appUserRepository.save(appUser);
    }

    /**
     * @param id metoden tar in IDt på användaren som ska tas bort från databasen och utför operationen.
     */
    public void deleteUser(Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user with " + id + " found"));
        appUserRepository.deleteById(id);
    }

}
