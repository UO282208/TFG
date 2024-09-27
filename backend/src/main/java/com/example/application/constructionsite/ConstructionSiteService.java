package com.example.application.constructionsite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructionSiteService {
    
    private final ConstructionSiteRepository constructionSiteRepository;

    @Autowired
    public ConstructionSiteService (ConstructionSiteRepository constructionSiteRepository){
        this.constructionSiteRepository = constructionSiteRepository;
    }

    public List<ConstructionSite> getConstructionSites(){
        return this.constructionSiteRepository.findAll();
    }

    public void addConstructionSite(ConstructionSite constructionSite){
        this.constructionSiteRepository.save(constructionSite);
    }
}
