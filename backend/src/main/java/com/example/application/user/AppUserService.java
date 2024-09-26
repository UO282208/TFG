package com.example.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService (AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAppUsers(){
        return this.appUserRepository.findAll();
    }

    public void addAppUser(AppUser appUser) {
        this.appUserRepository.save(appUser);
    }

}
