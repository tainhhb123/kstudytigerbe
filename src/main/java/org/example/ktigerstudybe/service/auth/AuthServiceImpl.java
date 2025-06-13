package org.example.ktigerstudybe.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.ktigerstudybe.dto.req.SignInRequest;
import org.example.ktigerstudybe.dto.req.SignUpRequest;
import org.example.ktigerstudybe.dto.resp.AuthResponse;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.UserRepository;
import org.example.ktigerstudybe.service.auth.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng.");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // mặc định

        userRepository.save(user);

        AuthResponse resp = new AuthResponse();
        resp.setUserId(user.getUserId());
        resp.setEmail(user.getEmail());
        resp.setFullName(user.getFullName());
        resp.setToken("dummy-token-for-now"); // Thay JWT thật vào đây

        return resp;
    }

    @Override
    public AuthResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai email hoặc mật khẩu."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai email hoặc mật khẩu.");
        }

        AuthResponse resp = new AuthResponse();
        resp.setUserId(user.getUserId());
        resp.setEmail(user.getEmail());
        resp.setFullName(user.getFullName());
        resp.setToken("dummy-token-for-now"); // Thay JWT thật vào đây

        return resp;
    }
}
