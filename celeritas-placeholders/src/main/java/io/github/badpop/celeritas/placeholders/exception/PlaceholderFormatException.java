package io.github.badpop.celeritas.placeholders.exception;

public class PlaceholderFormatException extends RuntimeException {

  public PlaceholderFormatException(String message) {
    super(message);
  }

  public PlaceholderFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
