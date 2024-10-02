package com.example.application.constructionsite;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstructionSiteRepository extends JpaRepository<ConstructionSite, Long>{

    Optional<ConstructionSite> getByName(String name);
    
}
