package com.example.spring_boot.UnitTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.application.auth.AuthenticationService;
import com.example.application.auth.RegistrationRequest;
import com.example.application.role.Role;
import com.example.application.role.RoleRepository;
import com.example.application.security.JwtService;
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;

class RegistrationServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUserWhenUserDoesNotExist() {
        var role = Role.builder().name("USER").build();
        var request = RegistrationRequest.builder().name("Prueba").email("prueba@gmail.com").password("prueba123")
                .build();

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        authenticationService.register(request);

        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void shouldNotRegisterUserWhenUserAlreadyExists() {
        var role = Role.builder().name("USER").build();
        var request = RegistrationRequest.builder().name("Prueba").email("prueba@gmail.com").password("prueba123").build();
        var existingUser = AppUser.builder().name("Prueba").email("prueba@gmail.com").password("prueba123").roles(List.of(Role.builder().name("USER").build())).build();

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(DataIntegrityViolationException.class, () -> authenticationService.register(request));

        verify(appUserRepository, never()).save(any(AppUser.class));
    }
}