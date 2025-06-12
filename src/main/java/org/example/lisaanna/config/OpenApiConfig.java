package org.example.lisaanna.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Klassen konfigurerar OpenAPI och Swagger-dokumentation för att kunna använda JWT autentisering
 * i Swagger.
 *
 * Annotationen OpenAPIDefinition sätter global metadata för APIet:
 * APIets titel som den visas i Swaggers UI + APIets version
 * deklarerar en global SecurityRequirement som heter "bearerAuth". Detta innebär att alla endpoints
 * kräver JWT autentisering om inte annat anges
 *
 * Annotationen SecurityScheme definierar hur säkerheten fungerar.
 * name = "bearerAuth"
 * Namnet används för att identifiera just denna SecurityScheme. Den säger åt Swagger att använda
 * SecuritySchemen som heter bearerAuth för autentiseringsrequests.
 *
 * type = SecuritySchemeType.HTTP
 * Den här specifierar autentiseringstypen. Här innebär det att autentiseringen hanteras genom en
 * HTTP header.
 *
 * scheme = "bearer"
 * Den här säger åt Swagger att förvänta sig att autentiseringsheadern använder formatet Bearer.
 *
 * bearerFormat = "JWT"
 * Den här säger åt Swagger att tokenen är i JWT-format.
 */
@OpenAPIDefinition(
        info = @Info(title = "API med JWT", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    // för att kunna använda JWT med Swagger
}
