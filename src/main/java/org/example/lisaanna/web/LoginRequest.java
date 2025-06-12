package org.example.lisaanna.web;

/**
 * records –
 * ett nytt sätt att skapa oföränderliga (immutable) datastrukturer med minimal kod.
 * Detta skapar automatiskt:
 * en klass LoginRequest
 * två final fält: username och password
 * en konstruktor
 * get -metoder ( username() , password() )
 * toString() , equals() , och hashCode()
 * LoginRequest används när klienten skickar användarnamn och lösenord med en POST-
 * request till applikationen i syfte att få tillbaka en jwt-token
 * @param username
 * @param password
 */
public record LoginRequest(String username, String password) {
}
