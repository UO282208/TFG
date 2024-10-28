package com.example.spring_boot.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.application.auth.AuthenticationRequest;
import com.example.application.auth.AuthenticationService;
import com.example.application.security.JwtService;
import com.example.application.user.AppUser;

public class AuthenticateServiceTest {
    
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
    void shouldAuthenticateAndReturnTokenForExistingUser() {
        var request = AuthenticationRequest.builder().email("prueba@gmail.com").password("prueba123").build();
        var user = AppUser.builder().name("Prueba").email("prueba@gmail.com").password("prueba123").build();

        var authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        var token = "mock-jwt-token";
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn(token);

        var response = authenticationService.authenticate(request);
        assertNotNull(response);
        assertEquals(token, response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExistWhenAuthenticating() {
        var request = AuthenticationRequest.builder().email("prueba@gmail.com").password("prueba123").build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("El usuario o la contraseña son incorrectos"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyMap(), any(AppUser.class));
    }
}