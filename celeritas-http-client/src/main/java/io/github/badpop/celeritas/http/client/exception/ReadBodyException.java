package io.github.badpop.celeritas.http.client.exception;

public class ReadBodyException extends Exception {

  public ReadBodyException() {
  }

  public ReadBodyException(String message) {
    super(message);
  }

  public ReadBodyException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReadBodyException(Throwable cause) {
    super(cause);
  }
}
