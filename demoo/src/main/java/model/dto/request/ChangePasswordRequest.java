package Practice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 50, message = "Mật khẩu mới phải từ 6-50 ký tự")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    // ===== Constructors =====
    public ChangePasswordRequest() {}

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // ===== Getters & Setters =====
    public String getOldPassword() { return oldPassword;}

    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword;}

    public String getNewPassword() { return newPassword;}

    public void setNewPassword(String newPassword) { this.newPassword = newPassword;}

    public String getConfirmPassword() { return confirmPassword;}
    public void setConfirmPassword(String confirmPassword) {this.confirmPassword = confirmPassword;}
}
