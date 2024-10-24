package com.example.application.auth;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.application.role.RoleRepository;
import com.example.application.security.JwtService;
import com.example.application.user.AppUser;
import com.example.application.user.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("Rol USER no inicializado"));
        var user = AppUser.builder().name(request.getName())
                                    .email(request.getEmail())
                                    .password(passwordEncoder.encode(request.getPassword()))
                                    .roles(List.of(userRole)).build();
        appUserRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = ((AppUser)auth.getPrincipal());
        claims.put("name", user.getName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
