package io.github.badpop.celeritas.placeholders.exception;

public class NullParametersException extends RuntimeException {

  public NullParametersException() {
    super("Unable to format a string if parameters null");
  }
}
