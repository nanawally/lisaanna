package org.example.lisaanna.exception;

/**
 * En subklass till den inbyggda superklassen RuntimeException. Skickas vidare till en
 * ExceptionHandler för att kunna användas.
 */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
