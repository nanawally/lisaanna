package org.example.lisaanna.service;

import org.example.lisaanna.component.AppUserMapper;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.example.lisaanna.web.AppUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserMapper appUserMapper;

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

}
