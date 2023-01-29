package com.github.badpop.celeritas.http.client.exception;

import java.io.IOException;

public class JsonBodyPublisherCreationException extends IOException {

  public JsonBodyPublisherCreationException() {
  }

  public JsonBodyPublisherCreationException(String message) {
    super(message);
  }

  public JsonBodyPublisherCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonBodyPublisherCreationException(Throwable cause) {
    super(cause);
  }
}
