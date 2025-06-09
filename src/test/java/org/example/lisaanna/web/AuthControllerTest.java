package org.example.lisaanna.web;

import org.example.lisaanna.config.SecurityConfig;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @BeforeEach
    public void setup() {
        appUserRepository.deleteAll();

        AppUser user = new AppUser();
        user.setUsername("admin");
        user.setPassword(securityConfig.passwordEncoder().encode("password123"));
        user.setRole("ADMIN");
        appUserRepository.save(user);
    }

    @Test
    public void testLoginAuthenticationToken() throws Exception {
        String loginJson = """
                {
                    "username": "admin",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/request-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk());
    }
}
