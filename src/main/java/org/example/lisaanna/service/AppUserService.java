package org.example.lisaanna.service;

import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUser> getAppUserList(){
        return appUserRepository.findAll();
    }

    public AppUser getAppUserByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

}
