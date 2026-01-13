package Practice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyOtpRequest {

    @NotBlank(message = "Mã OTP không được để trống")
    @Size(min = 6, max = 6, message = "Mã OTP phải có 6 ký tự")
    private String otpCode;

    // ===== Constructors =====
    public VerifyOtpRequest() {
    }

    public VerifyOtpRequest(String otpCode) {
        this.otpCode = otpCode;
    }

    // ===== Getter & Setter =====
    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
