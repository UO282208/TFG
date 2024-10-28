package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
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
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;

import jakarta.persistence.EntityNotFoundException;


public class DeleteCSServiceTest {

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void shouldDeleteConstructionSiteSuccessfully() {

        ConstructionSite site = ConstructionSite.builder().id(1).name("CS1").numOfWorkers(1).build();
        AppUser user = AppUser.builder().email("prueba@gmail.com").constructionSites(new ArrayList<>(List.of(site))).build();
        site.setOwner(user);
        
        when(constructionSiteRepository.getReferenceById(1L)).thenReturn(site);
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        constructionSiteService.deleteConstructionSite(1L);

        verify(constructionSiteRepository).delete(site); 
        verify(constructionSiteDetailsRepository).delete(site.getDetails());
        assertFalse(user.getConstructionSites().contains(site));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhenDeletingConstructionSite() {
        ConstructionSite site = ConstructionSite.builder().id(1).name("CS1").build();
        site.setOwner(AppUser.builder().build());
        
        when(constructionSiteRepository.getReferenceById(1L)).thenReturn(site);
        when(appUserRepository.findByEmail(site.getOwner().getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> constructionSiteService.deleteConstructionSite(1L));
    }

    @Test
    void shouldThrowExceptionWhenSiteNotFoundWhenDeletingConstructionSite() {
        when(constructionSiteRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> constructionSiteService.deleteConstructionSite(1L));
    }
}
