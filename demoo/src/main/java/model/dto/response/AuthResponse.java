package Practice.model.dto.response;

public class AuthResponse {

    private String token;
    private String type;
    private UserResponse user;

    public AuthResponse() {
        this.type = "Bearer";
    }

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
        this.type = "Bearer";
    }

    public AuthResponse(String token, String type, UserResponse user) {
        this.token = token;
        this.type = type;
        this.user = user;
    }

    // ===== Getters & Setters =====
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
