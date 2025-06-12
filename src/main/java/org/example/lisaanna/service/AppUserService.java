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
 *
 */
@Service
public class AppUserService {

    private final AppUserMapper appUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    /**
     *
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
     * @return
     */
    public List<AppUser> getAppUserList() {
        return appUserRepository.findAll();
    }

    /**
     * @param username
     * @return
     */
    public AppUser getAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    /**
     * @param appUserDTO
     */
    public void saveUser(AppUserDTO appUserDTO) {
        AppUser appUser = appUserMapper.toAppUser(appUserDTO);
        appUserRepository.save(appUser);
    }

    /**
     * @param id
     */
    public void deleteUser(Long id) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user with " + id + " found"));
        appUserRepository.deleteById(id);
    }

}
