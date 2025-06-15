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

/**
 * Testklass för AuthController som hanterar inloggning med tokens.
 * Klassens testLoginAuthenticationToken() testar att autentiseringen fungerar korrekt
 * och att endpointen returnerar HTTP 200 OK om rätt inloggningsuppgifter anges.
 *
 * @SpringBootTest kör Spring-kontexten så beans laddas som vid riktig körning
 * @AutoConfigureMockMvc gör det möjligt att testa kontrollers utan en riktig
 * webbserver.
 * <p>
 * Databasen rensas mellan varje test för att städa upp och säkerställa att tidigare test
 * inte påverkar varandra.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private SecurityConfig securityConfig;

    /**
     * Metoden förbereder testmiljön inför varje test.
     * - Databasen rensas.
     * - En testanvändare med krypterat lösenord skapas.
     * - Sparar användaren i AppUserRepository så att endpointen har något att jobba med.
     */
    @BeforeEach
    public void setup() {
        appUserRepository.deleteAll();

        AppUser user = new AppUser();
        user.setUsername("admin");
        user.setPassword(securityConfig.passwordEncoder().encode("password123"));
        user.setRole("ADMIN");
        appUserRepository.save(user);
    }

    /**
     * Metoden skickar en POST-request till endpointen /request-token
     * Skapar ett JSON-objekt med samma uppgifter som testanvändaren.
     * Kontrollerar att HTTP-status är samma som förväntat (200 OK)
     * vilket innebär att autentiseringen har lyckats och JWT-token har returnerats.
     *
     * @throws Exception om något går fel.
     */
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
