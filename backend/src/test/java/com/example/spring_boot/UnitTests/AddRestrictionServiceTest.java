package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.restriction.NewRestrictionRequest;
import com.example.application.restriction.Restriction;
import com.example.application.restriction.RestrictionRepository;

import jakarta.persistence.EntityNotFoundException;

public class AddRestrictionServiceTest {

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @Mock
    private RestrictionRepository restrictionRepository;

    @InjectMocks
    private ConstructionSiteService constructionSiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
     @Test
    void shouldSaveRestrictionSuccessfully() {
        Long siteId = 1L;
        NewRestrictionRequest request = NewRestrictionRequest.builder()
        .transformers(2)
        .expansionTanks(1)
        .radiators(3)
        .connectionPoints(4)
        .firewalls(1)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(10))
        .shouldAppear(true).build();

        ConstructionSite cs = new ConstructionSite();
        ConstructionSiteDetails details = new ConstructionSiteDetails();
        details.setId(2L);
        cs.setDetails(details);

        when(constructionSiteRepository.getReferenceById(siteId)).thenReturn(cs);
        when(constructionSiteDetailsRepository.getReferenceById(2L)).thenReturn(details);

        constructionSiteService.addRestriction(siteId, request);

        assertEquals(1, details.getRestrictions().size());
        Restriction savedRestriction = details.getRestrictions().get(0);
        assertEquals(2, savedRestriction.getTransformers());
        assertEquals(1, savedRestriction.getExpansionTanks());
        assertEquals(3, savedRestriction.getRadiators());
        assertEquals(4, savedRestriction.getConnectionPoints());
        assertEquals(1, savedRestriction.getFirewalls());
        assertEquals(request.getStartDate(), savedRestriction.getStartDate());
        assertEquals(request.getEndDate(), savedRestriction.getEndDate());
        assertTrue(savedRestriction.isShouldAppear());
    }

    @Test
    void shouldThrowExceptionWhenConstructionSiteNotFoundWhenAddingRestriction() {
        NewRestrictionRequest request = NewRestrictionRequest.builder().build();
        
        when(constructionSiteRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> constructionSiteService.addRestriction(1L, request));
    }

    @Test
    void shouldThrowExceptionWhenConstructionSiteDetailsNotFoundWhenAddingRestriction() {
        NewRestrictionRequest request = NewRestrictionRequest.builder().build();
        
        ConstructionSite cs = new ConstructionSite();
        cs.setDetails(ConstructionSiteDetails.builder().id(1L).build());
        
        when(constructionSiteRepository.getReferenceById(1L)).thenReturn(cs);
        when(constructionSiteDetailsRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> constructionSiteService.addRestriction(1L, request));
    }
}
