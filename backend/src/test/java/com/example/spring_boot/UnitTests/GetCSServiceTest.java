package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.constructionsite.ConstructionSite;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsite.ConstructionSiteService;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.restriction.RestrictionRepository;
import com.example.application.security.JwtService;
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;

public class GetCSServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @Mock
    private RestrictionRepository restrictionRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    private AppUser userWithSites;
    private AppUser userWithoutSites;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userWithSites = new AppUser();
        userWithSites.setEmail("prueba@gmail.com");
        userWithSites.setConstructionSites(createDummyConstructionSites());

        userWithoutSites = new AppUser();
        userWithoutSites.setEmail("prueba2@gmail.com");
        userWithoutSites.setConstructionSites(new ArrayList<>());
    }

    @Test
    void shouldReturnEmptyConstructionSitesForUserWithoutConstructionSites() {
        when(appUserRepository.findByEmail(userWithoutSites.getEmail())).thenReturn(Optional.of(userWithoutSites));

        List<ConstructionSite> sitesForUserWithoutSites = constructionSiteService.getConstructionSites(userWithoutSites.getEmail());

        assertNotNull(sitesForUserWithoutSites);
        assertTrue(sitesForUserWithoutSites.isEmpty());
    }
        

    @Test
    void shouldReturnConstructionSitesForUser() {
        when(appUserRepository.findByEmail(userWithSites.getEmail())).thenReturn(Optional.of(userWithSites));

        List<ConstructionSite> sites = constructionSiteService.getConstructionSites(userWithSites.getEmail());

        assertNotNull(sites);
        assertEquals(2, sites.size());
        assertEquals("CS1", sites.get(0).getName());
        assertEquals("CS2", sites.get(1).getName());
    }

    @Test
    void shouldReturnOnlyConstructionSitesForCorrectUser() {
        List<ConstructionSite> sites = new ArrayList<>();
        sites.add(ConstructionSite.builder().name("CS4").numOfWorkers(4).build());
        userWithoutSites.setConstructionSites(sites);

        when(appUserRepository.findByEmail(userWithSites.getEmail())).thenReturn(Optional.of(userWithSites));
        when(appUserRepository.findByEmail(userWithoutSites.getEmail())).thenReturn(Optional.of(userWithoutSites));

        List<ConstructionSite> sitesForUserWithSites = constructionSiteService.getConstructionSites(userWithSites.getEmail());

        assertNotNull(sitesForUserWithSites);
        assertEquals(2, sitesForUserWithSites.size());

        List<ConstructionSite> sitesForUserWithoutSites = constructionSiteService.getConstructionSites(userWithoutSites.getEmail());

        assertNotNull(sitesForUserWithoutSites);
        assertEquals(1, sitesForUserWithoutSites.size());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhenGettingConstructionSite() {
        when(appUserRepository.findByEmail("prueba@gmail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> constructionSiteService.getConstructionSites("prueba@gmail.com"));
    }

    private List<ConstructionSite> createDummyConstructionSites() {
        List<ConstructionSite> sites = new ArrayList<>();
        sites.add(ConstructionSite.builder().name("CS1").numOfWorkers(1).build());
        sites.add(ConstructionSite.builder().name("CS2").numOfWorkers(2).build());
        return sites;
    }
}
