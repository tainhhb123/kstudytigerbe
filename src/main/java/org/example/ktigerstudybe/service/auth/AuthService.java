package org.example.ktigerstudybe.service.auth;

import org.example.ktigerstudybe.dto.req.SignInRequest;
import org.example.ktigerstudybe.dto.req.SignUpRequest;
import org.example.ktigerstudybe.dto.resp.AuthResponse;

public interface AuthService {
    AuthResponse signUp(SignUpRequest request);
    AuthResponse signIn(SignInRequest request);
}
