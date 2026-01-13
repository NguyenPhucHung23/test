package Practice.service;

import Practice.model.entity.OtpEntity;
import Practice.model.entity.UserEntity;
import Practice.repository.OtpRepository;
import Practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${otp.expiration:300000}") // ms (default 5 phút)
    private long otpExpiration;

    public PasswordResetService(
            UserRepository userRepository,
            OtpRepository otpRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1) Gửi OTP qua email
    @Transactional
    public void sendOtpToEmail(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // Xóa OTP cũ nếu có (theo email)
        otpRepository.deleteByEmail(email);

        String otpCode = generateOtp();

        OtpEntity otp = new OtpEntity();
        otp.setEmail(email);
        otp.setOtpCode(otpCode);

        LocalDateTime now = LocalDateTime.now();
        otp.setCreatedAt(now);
        otp.setExpiresAt(now.plusSeconds(otpExpiration / 1000));
        otp.setVerified(false);
        otp.setResetToken(null);

        otpRepository.save(otp);
        emailService.sendOtpEmail(email, otpCode);
    }

    // 2) Verify OTP -> trả reset token
    @Transactional
    public String verifyOtpAndGenerateToken(String otpCode) {
        OtpEntity otp = otpRepository.findByOtpCode(otpCode)
                .orElseThrow(() -> new RuntimeException("OTP không hợp lệ"));

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            otpRepository.delete(otp);
            throw new RuntimeException("OTP đã hết hạn");
        }

        String resetToken = UUID.randomUUID().toString();
        otp.setVerified(true);
        otp.setResetToken(resetToken);
        otpRepository.save(otp);

        return resetToken;
    }

    // 3) Reset password bằng token
    @Transactional
    public void resetPasswordWithToken(String resetToken, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        OtpEntity otp = otpRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã hết hạn"));

        // hết hạn -> xóa luôn
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            otpRepository.delete(otp);
            throw new RuntimeException("Token đã hết hạn");
        }

        if (!otp.isVerified()) {
            throw new RuntimeException("OTP chưa được xác thực");
        }

        UserEntity user = userRepository.findByEmail(otp.getEmail())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // xóa OTP sau khi reset thành công
        otpRepository.delete(otp);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
