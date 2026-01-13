package Practice.controller;

import Practice.model.dto.response.ApiResponse;
import Practice.model.dto.response.UserResponse;
import Practice.model.entity.UserEntity;
import Practice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<String>> getAdminDashboard() {
        return ResponseEntity.ok(
                ApiResponse.success("Admin dashboard", "Đây là trang dành cho ADMIN")
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        List<UserResponse> userResponses = users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                ApiResponse.success("Danh sách users", userResponses)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User không tồn tại"));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa user thành công", null));
    }

    private UserResponse mapToUserResponse(UserEntity user) {
        return new UserResponse(
                String.valueOf(user.getId()),
                user.getFullname(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles(),
                user.getCreatedAt()
        );
    }
}
