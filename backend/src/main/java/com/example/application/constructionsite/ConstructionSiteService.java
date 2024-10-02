package com.example.application.constructionsite;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.security.JwtService;
import com.example.application.user.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstructionSiteService {
    
    private final JwtService jwtService;
    private final ConstructionSiteRepository constructionSiteRepository;
    private final AppUserRepository appUserRepository;

    public List<ConstructionSite> getConstructionSites(String username){
        var user = appUserRepository.findByEmail(username).
        orElseThrow(() -> new IllegalStateException("User not found"));
        return user.getAllConstructionSites();
    }

    public void addConstructionSite(NewConstructionSiteRequest newConstructionSiteRequest){
        var user = appUserRepository.findByEmail(jwtService.extractUsername(newConstructionSiteRequest.getToken())).
        orElseThrow(() -> new IllegalStateException("User not found"));
        var site = ConstructionSite.builder().name(newConstructionSiteRequest.getName())
                                            .numOfWorkers(newConstructionSiteRequest.getNumOfWorkers())
                                            .owner(user).build();
        user.addConstructionSite(site);

        this.constructionSiteRepository.save(site);
        this.appUserRepository.save(user);
    }

    public void deleteConstructionSite(Long id){
        var site = constructionSiteRepository.getReferenceById(id);
        var user = appUserRepository.findByEmail(site.getOwner().getEmail())
        .orElseThrow(() -> new IllegalStateException("User not found"));
        site.setOwner(null);
        user.removeConstructionSite(site);
        constructionSiteRepository.delete(site);
    }
}
