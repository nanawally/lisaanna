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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * genererar ett publikt + privat RSA-nyckelpar
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    /**
     * bygger JSON Web Kay-format utifrån nyckelparet
     * transformerar nyckelparet till formatet som krävs för att encode jwt-tokens
     *
     * @param keyPair
     * @return
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
     * signerar en JWT
     * använder JWKSource för att skapa encodern
     *
     * @param jwkSource
     * @return
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * läser och verifierar en JWT
     * använder KeyPair för att skapa decodern
     *
     * @param keyPair
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }

    /**
     * autentiserar användare vid inloggning
     *
     * @param userDetailsService
     * @param passwordEncoder
     * @return
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
     * Översätter token:s claims till roller
     *
     * @return
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
     * @return
     */
    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Tillåter alla endpoints
                        .allowedOrigins("http://localhost:5173") // Anpassa efter frontendens URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Tillåtna HTTP-metoder
                        .allowedHeaders("*") // Tillåter alla headers
                        .allowCredentials(true);
            }
        };
    }*/


    /**
     * Definierar CORS-konfigurationen som en @Bean
     * Konfigurerar CORS för applikationen och tillåter HTTP-förfrågningar
     * från frontend "<a href="http://localhost:5173">...</a>"
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
