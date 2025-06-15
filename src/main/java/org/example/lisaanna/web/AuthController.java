package org.example.lisaanna.web;

import org.example.lisaanna.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Den här klassen genererar signerade JWT tokens baserade på autentiserad användarinformation.
 */
@RestController
@RequestMapping("/request-token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * @param loginRequest en POST-request skickas in och mappas till ett LoginRequestobjekt.
     * en Authentication token skapas och skickas till authenticationManager. generateToken() tar
     * den informationen och skapar en token
     * @return en signerad token returneras
     */
    @PostMapping
    public ResponseEntity<String> token(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        String token = tokenService.generateToken(auth);
        return ResponseEntity.ok(token);
    }
}
