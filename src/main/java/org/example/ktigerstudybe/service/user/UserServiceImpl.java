package org.example.ktigerstudybe.service.user;

import org.example.ktigerstudybe.dto.req.ChangePasswordRequest;
import org.example.ktigerstudybe.dto.req.ForgotPasswordRequest;
import org.example.ktigerstudybe.dto.req.UserRequest;
import org.example.ktigerstudybe.dto.resp.UserResponse;
import org.example.ktigerstudybe.model.PasswordResetToken;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.PasswordResetTokenRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;
  // Convert entity -> response DTO
  private UserResponse toResponse(User user) {
    UserResponse resp = new UserResponse();
    resp.setUserId(user.getUserId());
    resp.setFullName(user.getFullName());
    resp.setEmail(user.getEmail());
    resp.setGender(user.getGender());
    resp.setDateOfBirth(user.getDateOfBirth());
    resp.setAvatarImage(user.getAvatarImage());
    resp.setJoinDate(user.getJoinDate());
    resp.setRole(user.getRole());
    resp.setUserStatus(user.getUserStatus());
    resp.setUserName(user.getUserName());
    return resp;
  }

  // Convert request DTO -> entity (cho create)
  private User toEntity(UserRequest req) {
    User user = new User();
    user.setFullName(req.getFullName());
    user.setEmail(req.getEmail());
    user.setPassword(req.getPassword());
    user.setRole(req.getRole());
    user.setUserStatus(req.getUserStatus());
    user.setUserName(req.getUserName());

    user.setGender(req.getGender());
    user.setDateOfBirth(req.getDateOfBirth());
    user.setAvatarImage(req.getAvatarImage());
    user.setJoinDate(req.getJoinDate());
    return user;
  }

  @Override
  public Page<UserResponse> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable)
            .map(this::toResponse);
  }

  @Override
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    return toResponse(user);
  }

  @Override
  public UserResponse createUser(UserRequest request) {
    User user = toEntity(request);
    user = userRepository.save(user);
    return toResponse(user);
  }

  @Override
  public UserResponse updateUser(Long id, UserRequest request) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    // Không update password ở đây (nếu muốn update password nên có hàm riêng)
    user.setRole(request.getRole());
    user.setUserStatus(request.getUserStatus());
    user.setUserName(request.getUserName());

    user.setGender(request.getGender());
    user.setDateOfBirth(request.getDateOfBirth());
    user.setAvatarImage(request.getAvatarImage());
    user.setJoinDate(request.getJoinDate());

    user = userRepository.save(user);
    return toResponse(user);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public UserResponse freezeUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    user.setUserStatus(1); // 1 = frozen
    user = userRepository.save(user);
    return toResponse(user);
  }

  @Override
  public UserResponse unfreezeUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    user.setUserStatus(0); // 0 = active
    user = userRepository.save(user);
    return toResponse(user);
  }

  @Override
  public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
    return userRepository
            .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUserNameContainingIgnoreCase(
                    keyword, keyword, keyword, pageable)
            .map(this::toResponse);
  }

  @Override
  public UserResponse getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new NoSuchElementException("Không tìm thấy user với email: " + email)
            );
    return toResponse(user);
  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

    // So sánh mật khẩu cũ (đã mã hóa trong DB)
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new RuntimeException("Mật khẩu hiện tại không đúng");
    }

    // Mã hóa mật khẩu mới và lưu lại
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }


}
