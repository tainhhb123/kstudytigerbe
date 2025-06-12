package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.UserRequest;
import org.example.ktigerstudybe.dto.resp.UserResponse;
import org.example.ktigerstudybe.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  // Lấy tất cả user (có phân trang)
  @GetMapping
  public Page<UserResponse> getAllUsers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size) {
    return userService.getAllUsers(PageRequest.of(page, size));
  }

  // Lấy user theo id
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    try {
      UserResponse resp = userService.getUserById(id);
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Tạo mới user
  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest request) {
    return userService.createUser(request);
  }

  // Cập nhật user
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
    try {
      UserResponse updated = userService.updateUser(id, request);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Xóa user
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // Đóng băng user
  @PostMapping("/{id}/freeze")
  public ResponseEntity<UserResponse> freezeUser(@PathVariable Long id) {
    try {
      UserResponse resp = userService.freezeUser(id);
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Mở băng user
  @PostMapping("/{id}/unfreeze")
  public ResponseEntity<UserResponse> unfreezeUser(@PathVariable Long id) {
    try {
      UserResponse resp = userService.unfreezeUser(id);
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Tìm kiếm user (phân trang)
  @GetMapping("/search")
  public Page<UserResponse> searchUsers(
          @RequestParam String keyword,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size) {
    return userService.searchUsers(keyword, PageRequest.of(page, size));
  }
}
