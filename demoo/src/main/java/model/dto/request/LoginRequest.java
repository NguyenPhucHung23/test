package Practice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class LoginRequest implements Serializable {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    // ===== Constructors =====
    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // ===== Getters & Setters =====
    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password;}
}
