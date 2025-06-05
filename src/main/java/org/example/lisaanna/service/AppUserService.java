package org.example.lisaanna.service;

import org.example.lisaanna.component.AppUserMapper;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.example.lisaanna.web.AppUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserMapper appUserMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);
    public void run() {
        logger.info("Startar uppgift");
        try {
            logger.debug("Kör logik...");
        } catch (Exception e) {
            logger.error("Fel uppstod", e);
        }
    }

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUserService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public List<AppUser> getAppUserList(){
        return appUserRepository.findAll();
    }

    public AppUser getAppUserByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

    public void saveUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toAppUser(appUserDTO);
        appUserRepository.save(appUser);
    }

    public void deleteUser(AppUserDTO appUserDTO){
        AppUser appUser = appUserMapper.toAppUser(appUserDTO);
        appUserRepository.delete(appUser);
    }

}
