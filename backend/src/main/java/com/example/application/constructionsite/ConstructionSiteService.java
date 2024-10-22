package com.example.application.constructionsite;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.constructionsitedetails.ConstructionSiteDetails;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.restriction.Restriction;
import com.example.application.restriction.RestrictionRepository;
import com.example.application.security.JwtService;
import com.example.application.user.AppUserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConstructionSiteService {
    
    private final JwtService jwtService;
    private final ConstructionSiteRepository constructionSiteRepository;
    private final AppUserRepository appUserRepository;
    private final ConstructionSiteDetailsRepository constructionSiteDetailsRepository;
    private final RestrictionRepository restrictionRepository;

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
        var details = ConstructionSiteDetails.builder().build();
        user.addConstructionSite(site);
        site.setDetails(details);

        this.constructionSiteRepository.save(site);
        this.appUserRepository.save(user);
        this.constructionSiteDetailsRepository.save(details);
    }

    public void deleteConstructionSite(Long id){
        var site = constructionSiteRepository.getReferenceById(id);
        var user = appUserRepository.findByEmail(site.getOwner().getEmail())
        .orElseThrow(() -> new IllegalStateException("User not found"));
        site.setOwner(null);
        var details = site.getDetails();
        user.removeConstructionSite(site);
        constructionSiteRepository.delete(site);
        constructionSiteDetailsRepository.delete(details);
    }

    public void modifyConstructionSite(Long id, @Valid ModifyConstructionSiteRequest modifyConstructionSiteRequest) {
        var site = constructionSiteRepository.getReferenceById(id);
        site.setName(modifyConstructionSiteRequest.getName());
        site.setNumOfWorkers(modifyConstructionSiteRequest.getNumOfWorkers());
        constructionSiteRepository.save(site);
    }

    public ConstructionSiteDetails getConstructionSiteDetailsById(Long id) {
        var site = constructionSiteRepository.getReferenceById(id);
        var details = site.getDetails();
        return details;
    }

    public void addRestriction(Long id, @Valid NewRestrictionRequest newRestrictionRequest) {
        var cs = constructionSiteRepository.getReferenceById(id);
        var details = constructionSiteDetailsRepository.getReferenceById(cs.getDetails().getId());
        var res = Restriction.builder().transformers(newRestrictionRequest.getTransformers())
                                       .expansionTanks(newRestrictionRequest.getExpansionTanks())
                                       .radiators(newRestrictionRequest.getRadiators())
                                       .connectionPoints(newRestrictionRequest.getConnectionPoints())
                                       .firewalls(newRestrictionRequest.getFirewalls())
                                       .startDate(newRestrictionRequest.getStartDate())
                                       .endDate(newRestrictionRequest.getEndDate())
                                       .shouldAppear(newRestrictionRequest.isShouldAppear())
                                       .site(details).build();
        details.addRestriction(res);
        constructionSiteDetailsRepository.save(details);
        restrictionRepository.save(res);
    }
}
