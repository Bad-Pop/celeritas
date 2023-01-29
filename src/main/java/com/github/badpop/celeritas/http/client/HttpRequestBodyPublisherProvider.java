package com.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.celeritas.http.client.exception.JsonBodyPublisherCreationException;
import io.vavr.control.Try;
import lombok.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Utility interface to build BodyPublishers from the Celeritas http client
 */
public interface HttpRequestBodyPublisherProvider {

  /**
   * Method to create a {@link BodyPublisher} containing the given object serialized into JSON.
   * The returned BodyPublisher can then be inserted into an {@link HttpRequest}.
   *
   * @param body the object you want to serialize into JSON. Must be of a serializable type. For more information take a look at {@link ObjectMapper#canSerialize}
   * @param <U>  a JSON serializable type
   * @return a new BodyPublisher containing the given object serialized into JSON
   * @throws JsonBodyPublisherCreationException if the given object is not JSON serializable or if the serialization failed
   */
  <U> BodyPublisher createJsonBodyPublisher(@NonNull U body) throws JsonBodyPublisherCreationException;

  /**
   * Performs the same operation as {@link #createJsonBodyPublisher(Object)} but wraps potential errors in a Vavr functional {@link Try}
   *
   * @param body the object you want to serialize into JSON. Must be of a serializable type. For more information take a look at {@link ObjectMapper#canSerialize}
   * @param <U>  a JSON serializable type
   * @return A functional {@link Try} containing either a {@link Throwable} or a {@link BodyPublisher} containing the JSON representation of the given object
   */
  <U> Try<BodyPublisher> tryToCreateJsonBodyPublisher(U body);

  /**
   * See {@link BodyPublishers#noBody}
   */
  default BodyPublisher createNoBodyBodyPublisher() {
    return BodyPublishers.noBody();
  }

  /**
   * See {@link BodyPublishers#ofString(String)}
   *
   * @throws NullPointerException if the given String is null
   */
  default BodyPublisher createStringBodyPublisher(@NonNull String body) {
    return BodyPublishers.ofString(body);
  }

  /**
   * See {@link BodyPublishers#ofInputStream(Supplier)}
   *
   * @throws NullPointerException if the given {@link InputStream} is null
   */
  default BodyPublisher createInputStreamBodyPublisher(@NonNull InputStream body) {
    return BodyPublishers.ofInputStream(() -> body);
  }

  /**
   * See {@link BodyPublishers#ofByteArray(byte[])}
   *
   * @throws NullPointerException if the given byte array is null
   */
  default BodyPublisher createByteArrayBodyPublisher(@NonNull byte[] body) {
    return BodyPublishers.ofByteArray(body);
  }

  /**
   * See {@link BodyPublishers#ofFile(Path)}
   *
   * @throws FileNotFoundException if the path is not found
   * @throws NullPointerException  if the path is null
   */
  default BodyPublisher createFileBodyPublisher(@NonNull Path filePath) throws FileNotFoundException {
    return BodyPublishers.ofFile(filePath);
  }
}
