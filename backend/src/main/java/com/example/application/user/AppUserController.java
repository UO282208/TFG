package com.example.application.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appUsers")
public class AppUserController {
    
    private final AppUserService appUserService;

    public AppUserController (AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping("/allUsers")
    @ResponseBody
    public ResponseEntity<List<AppUser>> getAppUsers(){
        return ResponseEntity.ok(appUserService.getAppUsers());
    }

    @PostMapping("/addUser")
    public void addAppUser(@RequestBody AppUser appUser){
        this.appUserService.addAppUser(appUser);
    }
}
