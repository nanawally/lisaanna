package org.example.lisaanna.web;

import org.example.lisaanna.config.SecurityConfig;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.example.lisaanna.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**PUBLIC
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SecurityConfig securityConfig;

    private String token;

    /**
     *
     */
    @BeforeEach
    public void setup() {
        // rensa databasen mellan testerna
        appUserRepository.deleteAll();

        // skapa testanvändare
        AppUser user = new AppUser();
        //user.setId(1L);
        user.setUsername("admin");
        user.setPassword(securityConfig.passwordEncoder().encode("AdminPass12!!"));
        user.setRole("ADMIN");
        appUserRepository.save(user);

        // generera token
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin",
                "AdminPass12!!",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        token = tokenService.generateToken(auth);
    }

    /**
     * @throws Exception
     */
    @Test
    // ta bort och förlita oss endast på tokens
    //@WithMockUser(username = "user", roles = {"ADMIN"})
    public void testAddUser() throws Exception {
        String uniqueUsername = "newuser_" + System.currentTimeMillis();
        String userJson = String.format("""
                {
                    "username": "%s",
                    "password": "ValidPass12!!",
                    "role": "USER",
                    "consentGiven": true
                }
                """, uniqueUsername);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .header("Authorization", "Bearer " + token)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    /**
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        AppUser user = appUserRepository.findByUsername("admin");
        System.out.println(appUserRepository.findAll().size());

        mockMvc.perform(delete("/user/" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
