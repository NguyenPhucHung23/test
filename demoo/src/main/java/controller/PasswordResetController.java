package Practice.controller;

import Practice.model.dto.request.ForgotPasswordRequest;
import Practice.model.dto.request.VerifyOtpRequest;
import Practice.model.dto.request.ResetPasswordRequest;
import Practice.model.dto.response.ApiResponse;
import Practice.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Bước 1: Nhập email → gửi OTP
    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.sendOtpToEmail(request.getEmail());
            return ResponseEntity.ok(
                    ApiResponse.success("Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.", null)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // Bước 2: Nhập OTP → verify và trả về reset token
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {
            String resetToken = passwordResetService.verifyOtpAndGenerateToken(request.getOtpCode());
            return ResponseEntity.ok(
                    ApiResponse.success("Xác thực OTP thành công. Vui lòng đặt mật khẩu mới.", resetToken)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // Bước 3: Nhập mật khẩu mới + token từ header
    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            @RequestHeader("Reset-Token") String resetToken
    ) {
        try {
            passwordResetService.resetPasswordWithToken(
                    resetToken,
                    request.getNewPassword(),
                    request.getConfirmPassword()
            );

            return ResponseEntity.ok(
                    ApiResponse.success("Đặt lại mật khẩu thành công! Bạn có thể đăng nhập với mật khẩu mới.", null)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
