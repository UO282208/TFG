package com.example.spring_boot.IntegrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.application.Application;
import com.example.application.auth.AuthenticationRequest;
import com.example.application.auth.AuthenticationResponse;
import com.example.application.auth.AuthenticationService;
import com.example.application.auth.RegistrationRequest;
import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsite.NewConstructionSiteRequest;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.restriction.RestrictionRepository;
import com.example.application.role.Role;
import com.example.application.role.RoleRepository;
import com.example.application.user.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class FileUploadControllerIntegrationTest {

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
    }

    @AfterEach
    public void tearDown(){
        restrictionRepository.deleteAll();
        constructionSiteDetailsRepository.deleteAll();
        constructionSiteRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    public void shouldUploadFileSuccessfully() throws Exception {
        RegistrationRequest req = RegistrationRequest.builder().email("a@a.com").name("a").password("aaaaaa").build();

        authenticationService.register(req);

        AuthenticationRequest request = AuthenticationRequest.builder().email("a@a.com").password("aaaaaa").build();

        AuthenticationResponse res = authenticationService.authenticate(request);

        NewConstructionSiteRequest res2 = NewConstructionSiteRequest.builder().token(res.getToken()).name("CS1").numOfWorkers(1).build();

        mockMvc.perform(post("/api/constructionSite/ConstructionSite")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(res2)));

        Long id = constructionSiteRepository.findAll().get(0).getId();
        String ids = id.toString();

        MockMultipartFile file = new MockMultipartFile("file", "testfile.txt", "text/plain", "aaaaaaaaaaaaaaaaaaaaaa".getBytes());

        ResultActions result = mockMvc.perform(multipart("/api/files/uploadFile")
            .file(file)
            .param("csId", ids)
            .with(csrf())
            .contentType(MediaType.MULTIPART_FORM_DATA));

        result.andExpect(status().isOk());
    }
}
