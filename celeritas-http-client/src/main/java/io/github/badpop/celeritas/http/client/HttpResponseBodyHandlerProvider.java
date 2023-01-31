package io.github.badpop.celeritas.http.client;

import lombok.NonNull;

import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Utility interface to build BodyHandlers from the Celeritas http client
 */
public interface HttpResponseBodyHandlerProvider {

  /**
   * See {@link BodyHandlers#discarding()}
   */
  default BodyHandler<Void> createDiscardingBodyHandler() {
    return BodyHandlers.discarding();
  }

  /**
   * See {@link BodyHandlers#ofByteArray()}
   */
  default BodyHandler<byte[]> createByteArrayingBodyHandler() {
    return BodyHandlers.ofByteArray();
  }

  /**
   * See {@link BodyHandlers#ofString()}
   */
  default BodyHandler<String> createStringBodyHandler() {
    return BodyHandlers.ofString();
  }

  /**
   * See {@link BodyHandlers#ofString()}
   *
   * @throws NullPointerException if the given charset is null
   */
  default BodyHandler<String> createStringBodyHandler(@NonNull Charset cs) {
    return BodyHandlers.ofString(cs);
  }

  /**
   * See {@link BodyHandlers#ofFile(Path)}
   *
   * @throws NullPointerException if the given path is null
   */
  default BodyHandler<Path> createFileBodyHandler(@NonNull Path path) {
    return BodyHandlers.ofFile(path);
  }

  /**
   * See {@link BodyHandlers#replacing(Object)}
   */
  default <U> BodyHandler<U> createReplacingBodyHandler(U replacing) {
    return BodyHandlers.replacing(replacing);
  }

  /**
   * See {@link BodyHandlers#ofLines()}
   */
  default BodyHandler<Stream<String>> createLinesBodyHandler() {
    return BodyHandlers.ofLines();
  }
}
