package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.imageupload.storage.FileSystemStorageService;
import com.example.application.imageupload.storage.StorageException;
import com.example.application.imageupload.storage.StorageProperties;

public class StoreFileServiceTest {

    @Mock
    private StorageProperties storageProperties;

    @Mock
    private ConstructionSiteRepository constructionSiteRepository;

    @Mock
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @InjectMocks
    private FileSystemStorageService fileSystemStorageService;

    private Path testRootLocation;
    private Path testResultsLocation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testRootLocation = Path.of("testRoot");
        testResultsLocation = Path.of("testResults");
        
        when(storageProperties.getLocation()).thenReturn(testRootLocation.toString());
        when(storageProperties.getResultsLocation()).thenReturn(testResultsLocation.toString());
        
        fileSystemStorageService = new FileSystemStorageService(storageProperties, constructionSiteRepository, constructionSiteDetailsRepository);
        
        try {
            Files.createDirectories(testRootLocation);
        } catch (IOException e) {
            fail("Failed to create test directory");
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            if (Files.exists(Path.of("testRoot"))) {
                Files.walk(Path.of("testRoot"))
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                e.printStackTrace(); 
                            }
                        });
            }

            if (Files.exists(Path.of("testResults"))) {
                Files.walk(Path.of("testResults"))
                        .sorted((a, b) -> b.compareTo(a)) 
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                e.printStackTrace(); 
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    

    @Test
    void shouldThrowExceptionWhenFileIsEmpty() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        
        assertThrows(StorageException.class, () -> fileSystemStorageService.store(emptyFile));
    }

    @Test
    void shouldStoreFileSuccessfully() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("prueba.txt");
        when(file.getInputStream()).thenReturn(Files.newInputStream(Files.createTempFile(testRootLocation, "prueba", ".txt")));

        fileSystemStorageService.store(file);

        Path storedFile = testRootLocation.resolve("prueba.txt");
        assertTrue(Files.exists(storedFile));
        
        Files.deleteIfExists(storedFile);
    }

    @Test
    void shouldThrowExceptionWhenFileIsOutsideRoot() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("../prueba.txt");
        
        assertThrows(StorageException.class, () -> fileSystemStorageService.store(file));
    }

    @Test
    void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("prueba.txt");
        when(file.getInputStream()).thenThrow(new IOException("I/O error"));

        assertThrows(StorageException.class, () -> fileSystemStorageService.store(file));
    }
}
