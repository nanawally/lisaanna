package org.example.lisaanna.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    /*
    Ta emot en autentiserad användare
    Skapa en JWT-token med information om användaren
    Returnera token som sträng
    */

    private JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication auth) {
        // referenspunkt för när token skapas
        Instant now = Instant.now();

        // användarens roller samlas till en sträng
        // strängen lagras som ett claim i tokenen under 'scope'
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // bygger upp innehållet i JWT:n
        JwtClaimsSet claims = JwtClaimsSet.builder()
                // vem som skapat den
                .issuer("self")
                // när den skapades
                .issuedAt(now)
                // hur länge den är gilitg
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                // användarnamn
                .subject(auth.getName())
                // användarens roller
                .claim("scope", scope)
                .build();
        // JWT:n signeras med den privata nyckeln via encodern -> returneras som en sträng
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
