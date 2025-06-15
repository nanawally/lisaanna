package org.example.lisaanna.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

/**
 * Klassen innehåller mycket boilerplate-kod eftersom den skapar upp instanser av olika klasser och interfaces
 * som finns tillgängliga i Spring Security, t.ex.
 * nyckelpar, encoder och decoders.
 * Klassen innehåller konfigurationer kring åtkomst etc och sätter regler för säkerhet och autentisering.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Säkerhets-filterkedja som styr vilka sidor som kräver inloggning.
     * Stänger av CSRF för att kunna använda JWT-tokens.
     * Sessionshantering för att undvika att spara autentiseringsinformation,
     * istället måste en ny request göras varje gång.
     * @param http används för att komma åt metoderna som kallas på i filterkedjan.
     * @return HttpSecurity-objekt med alla konfigurationer inuti.
     * @throws Exception om något skulle gå fel.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Aktivera CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Inaktivera CSRF
                .csrf(csrf -> csrf.disable())
                // Sessionshantering
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Auktoriseringsregler
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/request-token").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Tillåt preflight requests
                        .anyRequest().authenticated())
                // JWT-konfiguration
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    /**
     * Använder BCrypt-algoritmen för att ange hur lösenord ska hashas.
     *
     * @return en instans av BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Genererar ett publikt och privat RSA-nyckelpar.
     * Privata nyckeln signerar eller dekrypterar.
     * Publika nyckeln verifierar eller krypterar.
     * Använder en asymmetrisk krypteringsalgoritm som kopplar nycklarna.
     * I Spring Security signeras JWT med den privata nyckeln,
     * och verifieras med den publika nyckeln.
     *
     * @return ett RSA-nyckelpar
     * @throws NoSuchAlgorithmException om algoritmen inte är tillgänglig i kontexten.
     */
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    /**
     * Metoden bygger ett JSON Web Key-format utifrån nyckelparet som genererats.
     * Nyckelparet omformateras till JWKSet som krävs för att encoda JWT-tokens.
     *
     * @param keyPair, som skapades i keyPair(), injeceras automatiskt av Spring
     * @return ett objekt av typen JWKSource som används för att signera och verifiera JWT-tokens.
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, context) -> jwkSelector.select(jwkSet));
    }

    /**
     * Metoden används för att signera en JWT.
     * JWKSource-beanen används för att skapa encodern.
     *
     * @param jwkSource, som skapades i jwkSource(), injeceras automatiskt av Spring
     * @return en encoder för specifikt JWT-tokens.
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Metoden används för att läsa av och verifiera en JWT.
     * Den tidigare beanen använder automatiskt det KeyPair-paret som skapades,
     * för att göra en encoder.
     * Denna metod använder samma KeyPair-par för att skapa en decoder.
     *
     * @param keyPair, som skapades i keyPair(), injeceras automatiskt av Spring
     * @return en decoder för specifikt JWT-tokens.
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }

    /**
     * Metoden skapar och konfigurerar en AuthenticationManager
     * som ansvarar för att autentisera användare vid inloggning.
     * Skapar DaoAuthenticationProvider som är en implementering av AuthenticationProvider.
     * Den kopplar sedan in parametrarna i den.
     *
     * @param userDetailsService för att hämta användarinformation
     * @param passwordEncoder för att jämföra lösenord
     * @return ett Authentication-objekt som är autentiserat
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    /**
     * Metoden kontrollerar vilken token som kommer in
     * och returnerar olika roller beroende på token:en.
     * Dessa används i Spring Security för att styra åtkomst.
     *
     * @return en instans av JwtAuthenticationConverter som innehåller den
     * angivna konfigurationen.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

    /**
     * Definierar CORS-konfigurationen som en @Bean
     * Konfigurerar CORS för applikationen och tillåter HTTP-förfrågningar
     * från frontend "href="http://localhost:5173">"
     * Alla headers tillåts
     * JWT-token i Authorization-headern kan skickas
     * JWT-token exponeras för att klienten ska kunna läsa token från svaret
     *
     * @return en CorsConfigurationSource som en @Bean, som Spring Security använder
     * i CORS-hanteringen
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // skapar ett tomt CORS-konfigurationsobjekt
        CorsConfiguration configuration = new CorsConfiguration();
        // tillåter endast requests från localhost:5173
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // tillåtna HTTP-metoder
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // tillåter alla headers
        configuration.setAllowedHeaders(List.of("*"));
        // tillåter autentisering med token
        configuration.setAllowCredentials(true);
        // klienten kan läsa Authorization-headern
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // kopplar reglerna från ovan till alla endpoints
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
