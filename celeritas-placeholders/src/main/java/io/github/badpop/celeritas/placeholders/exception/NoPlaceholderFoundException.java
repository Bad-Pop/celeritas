package io.github.badpop.celeritas.placeholders.exception;

public class NoPlaceholderFoundException extends RuntimeException {

  public NoPlaceholderFoundException() {
    super("No placeholder was found into the given string format");
  }
}
