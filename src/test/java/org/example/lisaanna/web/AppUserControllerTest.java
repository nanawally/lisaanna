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

/**
 * Testklass för AppUserController. Klassen testar metoderna addUser() och deleteUser().
 *
 * Klassen använder MockMvc och Spring Boot Test för att bygga en mockad testmiljö.
 * För att kunna genomföra testen så måste en admin-användare skapas för att generera en
 * JWT-token, som sedan används vid autentisering av HTTP-anropen.
 *
 * Databasen rensas mellan varje test för att städa upp och säkerställa att tidigare test
 * inte påverkar varandra.
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
     * Metoden förbereder testmiljön inför varje test.
     * - Databasen rensas.
     * - En användare skapas, för att kunna generera en JWT-token
     * - Token används i autentiserade HTTP-anrop.
     */
    @BeforeEach
    public void setup() {
        // rensa databasen mellan testerna
        appUserRepository.deleteAll();

        // skapa test-admin för att generera en token
        AppUser user = new AppUser();
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
     * Testar att en användare registreras via POST /user.
     * <p>
     * Användare skapas som ett JSON-objekt enligt valideringsregler från AppUserDTO.
     * Skickar ett POST-anrop med JWT-token och CSRF-token.
     * Förväntat resultat är att servern returnerar HTTP 201 Created.
     *
     * @throws Exception som något skulle gå fel vid anropet.
     */
    @Test
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
     * Testar att en användare tas bort via DELETE /user/{id}.
     * <p>
     * Använder användaren "admin" från setup(),
     * hämtar ID:t och skickar ett DELETE-anrop tillsammans med token.
     * Förväntat resultat är att servern returnerar HTTP 204 No Content.
     *
     * @throws Exception om något skulle gå fel vid anropet.
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
