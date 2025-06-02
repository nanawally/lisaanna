package org.example.lisaanna.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AppUserDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Pattern(regexp = "^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:[^0-9]*[0-9]){2})(?=(?:[^!@#$%&*]*[!@#$%&*]){2}).{8,}$",
            message = "Password must be minimum 8 characters, include at least 1 capital letter, 2 numbers, and 2 special characters (!@#$%&*)")
    private String password;
    @Pattern(
            regexp = "^(admin|user|public)$",
            message = "Role must be one of: admin, user, or public"
    )
    private String role;
    private boolean consentGiven;

    public AppUserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isConsentGiven() {
        return consentGiven;
    }

    public void setConsentGiven(boolean consentGiven) {
        this.consentGiven = consentGiven;
    }
}
