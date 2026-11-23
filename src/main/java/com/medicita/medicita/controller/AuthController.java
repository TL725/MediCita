package com.medicita.medicita.controller;

import com.medicita.medicita.dto.LoginRequest;
import com.medicita.medicita.dto.LoginResponse;
import com.medicita.medicita.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.nombreUsuario(), request.password()));

        String rol = auth.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");

        String token = jwtService.generarToken(request.nombreUsuario(), rol);
        return ResponseEntity.ok(new LoginResponse(token, rol));
    }
}