package com.example.spring_boot.IntegrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import com.example.application.Application;
import com.example.application.auth.AuthenticationRequest;
import com.example.application.auth.AuthenticationResponse;
import com.example.application.auth.AuthenticationService;
import com.example.application.auth.RegistrationRequest;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsite.ModifyConstructionSiteRequest;
import com.example.application.constructionsite.NewConstructionSiteRequest;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.handler.ExceptionResponse;
import com.example.application.restriction.NewRestrictionRequest;
import com.example.application.restriction.RestrictionRepository;
import com.example.application.role.Role;
import com.example.application.role.RoleRepository;
import com.example.application.user.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ConstructionSiteControllerIntegrationTest {
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ConstructionSiteRepository constructionSiteRepository;

    @Autowired
    private ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

    @Autowired
    private RestrictionRepository restrictionRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        if (!roleRepository.findByName("USER").isPresent()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        RegistrationRequest req = RegistrationRequest.builder().email("a@a.com").name("a").password("aaaaaa").build();

        authenticationService.register(req);
    }

    @AfterEach
    public void tearDown(){
        restrictionRepository.deleteAll();
        constructionSiteDetailsRepository.deleteAll();
        constructionSiteRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    public void shouldAddCSSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite//ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        result.andExpect(status().isAccepted());

        assertEquals(1, constructionSiteRepository.count());
        assertTrue(constructionSiteRepository.getByName("CS1").isPresent());
    }

    @Test
    public void shouldAddTwoCSWithSameNameSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        result.andExpect(status().isAccepted());

        assertEquals(1, constructionSiteRepository.count());

        ResultActions result2 = mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        result2.andExpect(status().isAccepted());

        assertEquals(2, constructionSiteRepository.count());
    }

    @Test
    public void shouldNotAddCSWithEmptyFields() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("").numOfWorkers(0).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        result.andExpect(status().isBadRequest());

        String responseContent = result.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertEquals(2, exceptionResponse.getValidationErrors().size());

        assertTrue(exceptionResponse.getValidationErrors().contains("El campo nombre no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo nÃºmero de trabajadores debe tener un valor superior a 0"));
    }

    @Test
    public void shouldGetCSSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        ResultActions result = mockMvc.perform(get("/api/constructionSite/ConstructionSites")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + res.getToken()));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteCSSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        ResultActions result = mockMvc.perform(delete("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", id.toString()));

        result.andExpect(status().isOk());
        assertEquals(0, constructionSiteRepository.count());
    }

    @Test
    public void shouldModifyCSSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        ModifyConstructionSiteRequest req = ModifyConstructionSiteRequest.builder().name("CS23").numOfWorkers(12).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/modifyConstructionSite/" + id.toString())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)));

        result.andExpect(status().isAccepted());

        assertTrue(constructionSiteRepository.getByName("CS23").isPresent());
    }

    @Test
    public void shouldNotModifyCSWithEmptyFields() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        ModifyConstructionSiteRequest req = ModifyConstructionSiteRequest.builder().name("").numOfWorkers(0).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/modifyConstructionSite/" + id.toString())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)));

        result.andExpect(status().isBadRequest());

        assertFalse(constructionSiteRepository.getByName("CS23").isPresent());

        String responseContent = result.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertEquals(2, exceptionResponse.getValidationErrors().size());

        assertTrue(exceptionResponse.getValidationErrors().contains("El campo nombre no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo nÃºmero de trabajadores debe tener un valor superior a 0"));
    }

    @Test
    public void shouldGetCSDetailsSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        ResultActions result = mockMvc.perform(get("/api/constructionSite/" + id.toString())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldAddRestrictionSuccessfully() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        NewRestrictionRequest req = NewRestrictionRequest.builder().transformers(1).expansionTanks(1).radiators(1)
        .connectionPoints(1).firewalls(1).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).shouldAppear(true).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/" + id.toString() + "/Restriction") 
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)));

        result.andExpect(status().isAccepted());
        assertEquals(1, restrictionRepository.count());
    }

    @Test
    public void shouldNotAddRestrictionWithEmptyFields() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
        .email("a@a.com")
        .password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();

        NewRestrictionRequest req = NewRestrictionRequest.builder().transformers(-1).expansionTanks(-1).radiators(-1)
        .connectionPoints(-1).firewalls(-1).startDate(null).endDate(null).shouldAppear(true).build();

        ResultActions result = mockMvc.perform(post("/api/constructionSite/" + id.toString() + "/Restriction") 
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)));

        result.andExpect(status().isBadRequest());
        assertEquals(0, restrictionRepository.count());
    
        String responseContent = result.andReturn().getResponse().getContentAsString();
    
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);
    
        assertEquals(7, exceptionResponse.getValidationErrors().size());
    
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo fecha de fin no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo puntos de conexiÃ³n debe tener un valor igual o superior a 0"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo muros cortafuegos tener un valor igual o superior a 0"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo fecha de inicio no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo radiadores debe tener un valor igual o superior a 0"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo tanques de expansiÃ³n debe tener un valor igual o superior a 0"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo transformadores debe tener un valor igual o superior a 0"));
    }
}
