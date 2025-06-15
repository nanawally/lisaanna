package org.example.lisaanna.web;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * För att undvika att använda domänklassen (AppUser) direkt i weblagret används den här DTO-klassen.
 * Utöver att separera weblagret som mer intern logik ger klassen oss bättre kontroll över vilka
 * fält som tas emot. Den sköter också valideringen av dessa fält.
 */
public class AppUserDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Pattern(regexp = "^(?=(?:[^A-Z]*[A-Z]){1})(?=(?:[^0-9]*[0-9]){2})(?=(?:[^!@#$%&*]*[!@#$%&*]){2}).{8,}$",
            message = "Password must be minimum 8 characters, include at least 1 capital letter, 2 numbers, and 2 special characters (!@#$%&*)")
    private String password;
    @Pattern(
            regexp = "^(ADMIN|USER|PUBLIC)$",
            message = "Role must be one of: admin, user, or public"
    )
    private String role;
    @AssertTrue(message = "Consent must be given")
    private boolean consentGiven;

    public AppUserDTO() {
    }

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
