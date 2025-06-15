package org.example.lisaanna.web;

/**
 * Den här klassen innehåller endast en record, som är en typ av immutable datastruktur.
 * En record skapar automatiskt en klass (LoginRequest), två final fält (username och password), en
 * konstruktor, get-metoder, samt toString(), equals(), och hashCode().
 * Den används när en POST-request skickas till applikationen för att få tillbaka en JWT-token.
 */
public record LoginRequest(String username, String password) {
}
