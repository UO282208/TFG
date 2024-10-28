package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.application.constructionsitedetails.ConstructionSiteDetails;
import com.example.application.constructionsite.ConstructionSite;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.imageupload.storage.FileSystemStorageService;
import com.example.application.imageupload.storage.StorageProperties;

public class ProcessFileServiceTest {
    
    @Mock
    private StorageProperties storageProperties;

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @InjectMocks
    private FileSystemStorageService fileSystemStorageService;

    private ConstructionSiteDetails details;
    private ConstructionSite constructionSite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        details = new ConstructionSiteDetails();
        details.setRestrictionsViolated(new ArrayList<>());
        details.setNumberOfTransformers(3);
        details.setNumberOfExpansionTanks(2);
        details.setNumberOfRadiators(1);
        details.setNumberOfConnectionPoints(0);
        details.setNumberOfFirewalls(1);
        
        constructionSite = new ConstructionSite();
        constructionSite.setDetails(details);
        
        when(constructionSiteRepository.getReferenceById(anyLong())).thenReturn(constructionSite);
    }

    @Test
    void shouldProcessFileSuccessfully() throws Exception {
        String filename = "prueba.txt";

        when(constructionSiteRepository.getReferenceById(anyLong())).thenReturn(constructionSite);
        details.setLastDayUploaded(LocalDateTime.now().minusDays(1));

        fileSystemStorageService.processFile(filename, "1");

        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.toLocalDate(), details.getLastDayUploaded().toLocalDate());
        assertEquals(3, details.getNumberOfTransformers());
        assertEquals(2, details.getNumberOfExpansionTanks());
        assertEquals(1, details.getNumberOfRadiators());
        assertEquals(0, details.getNumberOfConnectionPoints());
        assertEquals(1, details.getNumberOfFirewalls());
    }

    @Test
    void shouldHandlePythonProcessError() throws Exception {
        String filename = "prueba";

        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        when(processBuilder.start()).thenThrow(new IOException("Error en el script"));

        fileSystemStorageService.processFile(filename, "1");

        assertEquals(LocalDateTime.now().getHour(), details.getLastDayUploaded().getHour(), 1);
        verify(constructionSiteDetailsRepository).save(details);
    }
}
