package Practice.controller;

import Practice.model.dto.request.UpdateUserRequest;
import Practice.model.dto.request.ChangePasswordRequest;
import Practice.model.dto.response.ApiResponse;
import Practice.model.dto.response.UserResponse;
import Practice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponse user = userService.getUserProfile(email);
        return ResponseEntity.ok(ApiResponse.success("Thông tin profile", user));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UserResponse updatedUser = userService.updateUserProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin thành công", updatedUser));
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        userService.changePassword(email, request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }
}
