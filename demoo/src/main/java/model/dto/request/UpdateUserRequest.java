package Practice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 3, max = 100, message = "Tên phải từ 3-100 ký tự")
    private String fullname;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    // ===== Constructors =====
    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String fullname, String phone) {
        this.fullname = fullname;
        this.phone = phone;
    }

    // ===== Getters & Setters =====
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
