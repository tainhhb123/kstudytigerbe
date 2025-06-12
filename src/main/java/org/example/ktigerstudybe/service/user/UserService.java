package org.example.ktigerstudybe.service.user;

import org.example.ktigerstudybe.dto.req.UserRequest;
import org.example.ktigerstudybe.dto.resp.UserResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
	List<UserResponse> getAllUsers();
	UserResponse getUserById(Long id);
	UserResponse createUser(UserRequest request);
	UserResponse updateUser(Long id, UserRequest request);
	void deleteUser(Long id);
}
	// Lấy tất cả user (có phân trang)
	Page<UserResponse> getAllUsers(Pageable pageable);

	// Lấy user theo ID
	UserResponse getUserById(Long id);

	// Tạo mới user
	UserResponse createUser(UserRequest request);

	// Cập nhật user
	UserResponse updateUser(Long id, UserRequest request);

	// Xóa user
	void deleteUser(Long id);

	// Đóng băng tài khoản (freeze)
	UserResponse freezeUser(Long id);

	// Mở băng tài khoản (unfreeze)
	UserResponse unfreezeUser(Long id);

	// Tìm kiếm user theo keyword (phân trang)
	Page<UserResponse> searchUsers(String keyword, Pageable pageable);

}
