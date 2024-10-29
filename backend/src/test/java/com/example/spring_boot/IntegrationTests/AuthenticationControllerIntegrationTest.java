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

import com.example.application.Application;
import com.example.application.auth.AuthenticationRequest;
import com.example.application.auth.RegistrationRequest;
import com.example.application.handler.ExceptionResponse;
import com.example.application.role.Role;
import com.example.application.role.RoleRepository;
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        appUserRepository.deleteAll();
    }

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
        .name("Prueba")
        .email("prueba@gmail.com")
        .password("prueba123").build();

        ResultActions result = mockMvc.perform(post("/api/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isAccepted());

        var userOpt = appUserRepository.findByEmail("prueba@gmail.com");
        assertThat(userOpt).isPresent();
        
        var user = userOpt.get();
        assertThat(user.getName()).isEqualTo("Prueba");
        assertThat(user.getEmail()).isEqualTo("prueba@gmail.com");
        assertThat(user.getPassword()).isNotEqualTo("prueba123");
    }

    @Test
    public void shouldNotRegisterUserWhenEmailExists() throws Exception {
        AppUser existingUser = AppUser.builder()
            .name("Prueba2")
            .email("prueba2@gmail.com")
            .password("prueba123")
            .build();
        appUserRepository.save(existingUser);

        RegistrationRequest duplicateRequest = RegistrationRequest.builder()
        .name("Prueba")
        .email("prueba2@gmail.com")
        .password("prueba1234").build();

        ResultActions result = mockMvc.perform(post("/api/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(duplicateRequest)));


        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotRegisterUserWhenEmptyFields() throws Exception {
        RegistrationRequest emptyFieldsRequest = RegistrationRequest.builder()
        .name("")
        .email("")
        .password("").build();

        ResultActions result = mockMvc.perform(post("/api/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emptyFieldsRequest)));

        var userOpt = appUserRepository.findByEmail("");
        assertThat(userOpt).isNotPresent();

        result.andExpect(status().isBadRequest());

        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String responseContent = result.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertEquals(4, exceptionResponse.getValidationErrors().size());

        assertTrue(exceptionResponse.getValidationErrors().contains("El campo nombre no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo contraseÃ±a no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo email no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo contraseÃ±a debe tener 6 carÃ¡cteres como mÃ­nimo"));
    }

    @Test
    public void shouldNotRegisterUserWhenIncorrectEmail() throws Exception {
        RegistrationRequest emptyFieldsRequest = RegistrationRequest.builder()
        .name("Prueba3")
        .email("hola")
        .password("Prueba123").build();

        ResultActions result = mockMvc.perform(post("/api/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emptyFieldsRequest)));


        result.andExpect(status().isBadRequest());

        var userOpt = appUserRepository.findByEmail("hola");
        assertThat(userOpt).isNotPresent();

        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String responseContent = result.andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertEquals(1, exceptionResponse.getValidationErrors().size());

        assertTrue(exceptionResponse.getValidationErrors().contains("El campo email tiene un formato incorrecto"));
    }

    @Test
    public void shouldAuthenticateUserSuccessfully() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
        .name("Prueba")
        .email("prueba@gmail.com")
        .password("Prueba123").build();

        mockMvc.perform(post("/api/auth/register")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("prueba@gmail.com")
                .password("Prueba123").build();

        ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));

        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldNotAuthenticateUserWhenIncorrectEmail() throws Exception {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("hola")
                .password("Prueba123").build();

        ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));

        result.andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String responseContent = result.andReturn().getResponse().getContentAsString();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertEquals(1, exceptionResponse.getValidationErrors().size());
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo email tiene un formato incorrecto"));
    }

    @Test
    public void shouldNotAuthenticateUserWhenIncorrectPassword() throws Exception {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("prueba@gmail.com")
                .password("wrong-password") 
                .build();

        ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));

        result.andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    
        String responseContent = result.andReturn().getResponse().getContentAsString();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertTrue(exceptionResponse.getBussinessErrorDescription().contains("El usuario o la contraseÃ±a son incorrectos"));
    }

    @Test
    public void shouldNotAuthenticateUserWhenEmptyFields() throws Exception {
        AuthenticationRequest emptyFieldsRequest = AuthenticationRequest.builder()
                .email("")
                .password("").build();

        ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyFieldsRequest)));

        result.andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        String responseContent = result.andReturn().getResponse().getContentAsString();
        ExceptionResponse exceptionResponse = objectMapper.readValue(responseContent, ExceptionResponse.class);

        assertTrue(exceptionResponse.getValidationErrors().contains("El campo email no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo contraseÃ±a no debe estar vacÃ­o"));
        assertTrue(exceptionResponse.getValidationErrors().contains("El campo contraseÃ±a debe tener 6 carÃ¡cteres como mÃ­nimo"));
    }
}
