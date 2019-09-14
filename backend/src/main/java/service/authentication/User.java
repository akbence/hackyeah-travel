package service.authentication;

import javax.enterprise.inject.Model;
import java.time.LocalDateTime;

@Model
public class User {
    private String username;
    private String passwordHash;
    private LocalDateTime registration_date;
    private String token;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String password) { this.passwordHash = password; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(LocalDateTime registration_date) {
        this.registration_date = registration_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}