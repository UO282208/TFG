package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.constructionsite.ConstructionSite;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsite.ConstructionSiteService;
import com.example.application.constructionsite.ModifyConstructionSiteRequest;

import jakarta.persistence.EntityNotFoundException;

public class ModifyCSServiceTest {

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldModifyConstructionSiteSuccessfully() {
        Long siteId = 1L;
        ModifyConstructionSiteRequest modifyRequest = ModifyConstructionSiteRequest.builder().name("CS1").numOfWorkers(1).build();
        ConstructionSite existingSite = ConstructionSite.builder()
                .id(siteId)
                .name("CS20")
                .numOfWorkers(20)
                .build();

        when(constructionSiteRepository.getReferenceById(siteId)).thenReturn(existingSite);

        constructionSiteService.modifyConstructionSite(siteId, modifyRequest);

        assertEquals("CS1", existingSite.getName());
        assertEquals(1, existingSite.getNumOfWorkers());
        
        verify(constructionSiteRepository).save(existingSite);
    }

    @Test
    void shouldThrowExceptionWhenSiteNotFoundWhenModifyingConstructionSite() {
        Long siteId = 1L;
        ModifyConstructionSiteRequest modifyRequest = ModifyConstructionSiteRequest.builder().name("CS1").numOfWorkers(1).build();
        
        when(constructionSiteRepository.getReferenceById(siteId)).thenThrow(EntityNotFoundException.class);
        
        assertThrows(EntityNotFoundException.class, () -> constructionSiteService.modifyConstructionSite(siteId, modifyRequest));
    }
    
}
