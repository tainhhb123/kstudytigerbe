package org.example.ktigerstudybe.service.auth;

import org.example.ktigerstudybe.model.PasswordResetToken;
import org.example.ktigerstudybe.repository.PasswordResetTokenRepository;
import org.example.ktigerstudybe.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ktigerstudybe.dto.req.SignInRequest;
import org.example.ktigerstudybe.dto.req.SignUpRequest;
import org.example.ktigerstudybe.dto.resp.AuthResponse;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.UserRepository;
import org.example.ktigerstudybe.service.auth.AuthService;
import org.example.ktigerstudybe.service.userxp.UserXPService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserXPService userXPService;

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;


    @Override
    @Transactional
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

        //tao them userXP
        userXPService.createInitialUserXP(user.getUserId());

        AuthResponse resp = new AuthResponse();
        resp.setUserId(user.getUserId());
        resp.setEmail(user.getEmail());
        resp.setFullName(user.getFullName());
        resp.setToken("dummy-token-for-now");
        resp.setRole(user.getRole());

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
        resp.setToken("dummy-token-for-now");
        resp.setRole(user.getRole());

        return resp;
    }
    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy email này!"));

        // Xóa token cũ nếu có
        tokenRepository.deleteByUser(user);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(expiry);
        tokenRepository.save(prt);

        // Gửi email (nên chuyển emailService ra field @Autowired hoặc final)
//        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        String content = "Click vào link này để đặt lại mật khẩu (có hiệu lực 15 phút): " + resetLink;
        emailService.sendSimpleEmail(email, "Yêu cầu đặt lại mật khẩu", content);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không hợp lệ!"));

        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token đã hết hạn!");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword)); // mã hóa lại mật khẩu
        userRepository.save(user);

        tokenRepository.delete(prt);
    }
}
