package org.example.ktigerstudybe.controller;

import lombok.RequiredArgsConstructor;
import org.example.ktigerstudybe.dto.req.SignInRequest;
import org.example.ktigerstudybe.dto.req.SignUpRequest;
import org.example.ktigerstudybe.dto.resp.AuthResponse;
import org.example.ktigerstudybe.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.signIn(request));
    }
}
