package io.github.badpop.celeritas.http.client.response;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.badpop.celeritas.http.client.exception.ReadBodyException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;

import java.util.Optional;

/**
 * Utility interface to work with http responses more quickly
 */
public interface ResponseBodyJsonReader {

  /**
   * Utility method to deserialize the JSON response body to the requested class
   *
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return a new instance of the target class
   * @throws ReadBodyException    if an error occurred while trying to deserialize JSON body
   * @throws NullPointerException if the given target class is null
   */
  <U> U readBody(@NonNull Class<U> targetClass) throws ReadBodyException;

  /**
   * Utility method to deserialize the JSON response body to the requested {@link TypeReference}
   *
   * @param targetTypeReference the type reference to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return a new instance of the target type
   * @throws ReadBodyException    if an error occurred while trying to deserialize JSON body
   * @throws NullPointerException if the given target type reference is null
   */
  <U> U readBody(@NonNull TypeReference<U> targetTypeReference) throws ReadBodyException;

  /**
   * Utility method to deserialize the JSON response body to the requested class
   *
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return an empty Optional if an error occurred or a new Optional containing an instance of the target class
   */
  <U> Optional<U> readBodyOptional(Class<U> targetClass);

  /**
   * Utility method to deserialize the JSON response body to the requested type reference
   *
   * @param targetTypeReference the class to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return an empty Optional if an error occurred or a new Optional containing an instance of the target type reference
   */
  <U> Optional<U> readBodyOptional(TypeReference<U> targetTypeReference);

  /**
   * Utility method to deserialize the JSON response body to the requested class
   *
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return a None instance if an error occurred or a new Option containing an instance of the target class
   */
  <U> Option<U> readBodyOption(Class<U> targetClass);

  /**
   * Utility method to deserialize the JSON response body to the requested type reference
   *
   * @param targetTypeReference the class to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return a None instance if an error occurred or a new Option containing an instance of the target type reference
   */
  <U> Option<U> readBodyOption(TypeReference<U> targetTypeReference);

  /**
   * Same as {@link #readBody(Class)} but wraps potential errors in a Vavr functional {@link Try}
   *
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return A functional {@link Try} containing either a {@link Throwable} or a new instance of the target class
   */
  <U> Try<U> tryToReadBody(Class<U> targetClass);

  /**
   * Same as {@link #readBody(TypeReference)} but wraps potential errors in a Vavr functional {@link Try}
   *
   * @param targetTypeReference the type reference to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return A functional {@link Try} containing either a {@link Throwable} or a new instance of the target type
   */
  <U> Try<U> tryToReadBody(TypeReference<U> targetTypeReference);

  /**
   * Utility method to deserialize the JSON response body to the requested class
   * if the response code is the same as specified
   *
   * @param code        the response code for which the body must be deserialized
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return a new instance of the target class
   * @throws ReadBodyException    if the given code is not equal with the response code
   *                              or if an error occurred while trying to deserialize the response body
   * @throws NullPointerException if the given target class is null
   */
  <U> U readBodyForStatusCode(int code, @NonNull Class<U> targetClass) throws ReadBodyException;

  /**
   * Utility method to deserialize the JSON response body to the requested type reference
   * if the response code is the same as specified
   *
   * @param code                the response code for which the body must be deserialized
   * @param targetTypeReference the type reference to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return a new instance of the target class
   * @throws ReadBodyException    if the given code is not equal with the response code
   *                              or if an error occurred while trying to deserialize the response body
   * @throws NullPointerException if the given target type reference is null
   */
  <U> U readBodyForStatusCode(int code, @NonNull TypeReference<U> targetTypeReference) throws ReadBodyException;

  /**
   * Utility method to deserialize the JSON response body to the requested class
   * if the response code is the same as specified
   *
   * @param code        the response code for which the body must be deserialized
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return an Optional containing a new instance of the requested class if statuses correspond and no error occurred.
   * An empty Optional if an error occurred or statuses differs
   */
  <U> Optional<U> readBodyForStatusCodeOptional(int code, Class<U> targetClass);

  /**
   * Utility method to deserialize the JSON response body to the requested type reference
   * if the response code is the same as specified
   *
   * @param code                the response code for which the body must be deserialized
   * @param targetTypeReference the class to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return an Optional containing a new instance of the requested type reference if statuses correspond and no error occurred.
   * An empty Optional if an error occurred or statuses differs
   */
  <U> Optional<U> readBodyForStatusCodeOptional(int code, TypeReference<U> targetTypeReference);

  /**
   * Utility method to deserialize the JSON response body to the requested class
   * if the response code is the same as specified
   *
   * @param code        the response code for which the body must be deserialized
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return an Option containing a new instance of the requested class if statuses correspond and no error occurred.
   * An empty Option if an error occurred or statuses differs
   */
  <U> Option<U> readBodyForStatusCodeOption(int code, Class<U> targetClass);

  /**
   * Utility method to deserialize the JSON response body to the requested type reference
   * if the response code is the same as specified
   *
   * @param code                the response code for which the body must be deserialized
   * @param targetTypeReference the class to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return an Option containing a new instance of the requested type reference if statuses correspond and no error occurred.
   * An empty Option if an error occurred or statuses differs
   */
  <U> Option<U> readBodyForStatusCodeOption(int code, TypeReference<U> targetTypeReference);

  /**
   * Same as {@link #readBodyForStatusCode(int, Class)} but wraps potential errors in a Vavr functional {@link Try}
   *
   * @param code        the response code for which the body must be deserialized
   * @param targetClass the class to which the JSON body should be deserialized
   * @param <U>         the target class
   * @return A functional {@link Try} containing either a {@link Throwable} or a new instance of the target class
   */
  <U> Try<U> tryToReadBodyForStatusCode(int code, Class<U> targetClass);

  /**
   * Same as {@link #readBodyForStatusCode(int, TypeReference)} but wraps potential errors in a Vavr functional {@link Try}
   *
   * @param code                the response code for which the body must be deserialized
   * @param targetTypeReference the type reference to which the JSON body should be deserialized
   * @param <U>                 the target class
   * @return A functional {@link Try} containing either a {@link Throwable} or a new instance of the target type reference
   */
  <U> Try<U> tryToReadBodyForStatusCode(int code, TypeReference<U> targetTypeReference);
}
