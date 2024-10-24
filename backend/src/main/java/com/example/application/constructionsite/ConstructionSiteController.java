package com.example.application.constructionsite;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.application.constructionsitedetails.ConstructionSiteDetails;
import com.example.application.restriction.NewRestrictionRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/constructionSite")
@RequiredArgsConstructor
public class ConstructionSiteController {
    
    private final ConstructionSiteService constructionSiteService;

    @GetMapping("/allConstructionSites")
    @ResponseBody
    public ResponseEntity<List<ConstructionSite>> getConstructionSites(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<ConstructionSite> sites = constructionSiteService.getConstructionSites(userDetails.getUsername());

        return ResponseEntity.ok(sites);
    }

    @PostMapping("/addConstructionSite")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> addConstructionSite(@RequestBody @Valid NewConstructionSiteRequest newConstructionSiteRequest){
        this.constructionSiteService.addConstructionSite(newConstructionSiteRequest);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/deleteConstructionSite")
    public ResponseEntity<?> deleteConstructionSite(@RequestParam Long id){
        this.constructionSiteService.deleteConstructionSite(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modifyConstructionSite/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> modifyConstructionSite(@PathVariable Long id, @RequestBody @Valid ModifyConstructionSiteRequest modifyConstructionSiteRequest){
        this.constructionSiteService.modifyConstructionSite(id, modifyConstructionSiteRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/details/{id}")
    @ResponseBody
    public ResponseEntity<ConstructionSiteDetails> getConstructionSiteDetailsById(@PathVariable Long id) {
        ConstructionSiteDetails details = this.constructionSiteService.getConstructionSiteDetailsById(id);
        return ResponseEntity.ok(details);
    }

    @PostMapping("details/{id}/addRestriction")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> addRestriction(@PathVariable Long id, @RequestBody @Valid NewRestrictionRequest newRestrictionRequest) {
        this.constructionSiteService.addRestriction(id, newRestrictionRequest);
        return ResponseEntity.accepted().build();
    }
}
