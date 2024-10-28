package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.example.application.constructionsite.NewConstructionSiteRequest;
import com.example.application.constructionsitedetails.ConstructionSiteDetails;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.security.JwtService;
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;

public class AddCSServiceTest {
    
    @Mock
    private JwtService jwtService;

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    private AppUser user;
    private NewConstructionSiteRequest newConstructionSiteRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new AppUser();
        user.setEmail("prueba@gmail.com");
        user.setConstructionSites(new ArrayList<>());

        newConstructionSiteRequest = NewConstructionSiteRequest.builder()
                .token("token")
                .name("CS1")
                .numOfWorkers(1)
                .build();
    }

    @Test
    void shouldAddNewConstructionSite() {
        when(jwtService.extractUsername(newConstructionSiteRequest.getToken())).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        constructionSiteService.addConstructionSite(newConstructionSiteRequest);

        verify(constructionSiteRepository, times(1)).save(any(ConstructionSite.class));
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(constructionSiteDetailsRepository, times(1)).save(any(ConstructionSiteDetails.class));
        
        assertEquals(1, user.getConstructionSites().size());
        assertEquals("CS1", user.getConstructionSites().get(0).getName());
    }

    @Test
    void shouldAllowDuplicateConstructionSitesWithDifferentIds() {
        when(jwtService.extractUsername(newConstructionSiteRequest.getToken())).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(constructionSiteRepository.save(any(ConstructionSite.class))).thenAnswer(invocation -> {
            ConstructionSite site = invocation.getArgument(0);
            site.setId((long) (user.getConstructionSites().size() + 1));
            return site;
        });

        constructionSiteService.addConstructionSite(newConstructionSiteRequest);
        
        constructionSiteService.addConstructionSite(newConstructionSiteRequest);

        verify(constructionSiteRepository, times(2)).save(any(ConstructionSite.class));
        assertEquals(2, user.getConstructionSites().size());
        
        assertNotEquals(user.getConstructionSites().get(0).getId(), user.getConstructionSites().get(1).getId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhenAddingConstructionSite() {
        when(appUserRepository.findByEmail("prueba@gmail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> constructionSiteService.getConstructionSites("prueba@gmail.com"));
    }
}