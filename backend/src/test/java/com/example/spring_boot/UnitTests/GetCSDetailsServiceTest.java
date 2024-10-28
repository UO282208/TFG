package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.constructionsite.ConstructionSite;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsite.ConstructionSiteService;
import com.example.application.constructionsitedetails.ConstructionSiteDetails;

import jakarta.persistence.EntityNotFoundException;

public class GetCSDetailsServiceTest {

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnConstructionSiteDetailsSuccessfully() {
        ConstructionSiteDetails details = ConstructionSiteDetails.builder()
                .numberOfTransformers(1)
                .numberOfExpansionTanks(1)
                .numberOfRadiators(1)
                .numberOfConnectionPoints(3)
                .numberOfFirewalls(1)
                .lastDayUploaded(LocalDateTime.now())
                .build();
        ConstructionSite site = ConstructionSite.builder()
                .id(1L)
                .details(details)
                .build();

        when(constructionSiteRepository.getReferenceById(1L)).thenReturn(site);

        ConstructionSiteDetails result = constructionSiteService.getConstructionSiteDetailsById(1L);

        assertEquals(details, result);
    }

    @Test
    void shouldThrowExceptionWhenConstructionSiteNotFoundWhenGettingConstructionSiteDetails() {
        when(constructionSiteRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> constructionSiteService.getConstructionSiteDetailsById(1L));
    }
    
}
