package com.example.application.constructionsite;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/constructionSite")
public class ConstructionSiteController {
    
    private final ConstructionSiteService constructionSiteService;

    public ConstructionSiteController (ConstructionSiteService constructionSiteService)
    {
        this.constructionSiteService = constructionSiteService;
    }

    @GetMapping("/allConstructionSites")
    @ResponseBody
    public ResponseEntity<List<ConstructionSite>> getConstructionSites(){
        return ResponseEntity.ok(constructionSiteService.getConstructionSites());
    }

    @PostMapping("/addConstructionSite")
    public void addConstructionSite(@RequestBody ConstructionSite constructionSite){
        this.constructionSiteService.addConstructionSite(constructionSite);
    }
}
