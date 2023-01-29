package com.github.badpop.celeritas.http.client.exception;

public class UnsupportedBodyTypeException extends Exception {

  public UnsupportedBodyTypeException() {
  }

  public UnsupportedBodyTypeException(String message) {
    super(message);
  }

  public UnsupportedBodyTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedBodyTypeException(Throwable cause) {
    super(cause);
  }
}
