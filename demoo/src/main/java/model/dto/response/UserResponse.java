package Practice.model.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public class UserResponse {

    private String id;
    private String fullname;
    private String email;
    private String phone;
    private Set<String> roles;
    private LocalDateTime createdAt;

    // ===== Constructors =====
    public UserResponse() {
    }

    public UserResponse(String id, String fullname, String email,
                        String phone, Set<String> roles, LocalDateTime createdAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    // ===== Getters & Setters =====
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
