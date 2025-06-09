package org.example.lisaanna.web;

import org.example.lisaanna.config.SecurityConfig;
import org.example.lisaanna.entity.AppUser;
import org.example.lisaanna.repository.AppUserRepository;
import org.example.lisaanna.service.AppUserService;
import org.example.lisaanna.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
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

    @MockitoBean
    private AppUserService appUserService;


    @BeforeEach
    public void setup() {


        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        token = tokenService.generateToken(auth);

        // why u fuck it up ?????
        // appUserRepository.deleteAll();

        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword(securityConfig.passwordEncoder().encode("passwordAAA123!!!"));
        user.setRole("ADMIN");
        appUserRepository.save(user);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testAddUser() throws Exception {
        Mockito.when(appUserService.getAppUserByUsername("newuser")).thenReturn(null);
        Mockito.doNothing().when(appUserService).saveUser(Mockito.any(AppUserDTO.class));

        String userJson = """
                {
                    "username": "newuser",
                    "password": "passwordADKJSA1323321!!!!!"
                }
                """;

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(userJson)
                        .header("Authorization", "Bearer " + token)
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        AppUser user = appUserRepository.findByUsername("testuser");
        System.out.println(appUserRepository.findAll().size());

        //Mockito.when(appUserService.getAppUserList()).thenReturn(List.of(user));
        //Mockito.doNothing().when(appUserService).deleteUser(user.getId());

        mockMvc.perform(delete("/user/" + user.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
