package io.github.badpop.celeritas.http.client.response;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.badpop.celeritas.http.client.exception.ReadBodyException;
import io.github.badpop.celeritas.http.client.exception.UnsupportedBodyTypeException;
import io.github.badpop.celeritas.http.client.CeleritasHttpClient;
import io.vavr.API;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.*;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vavr.API.Failure;
import static io.vavr.API.Try;
import static lombok.AccessLevel.NONE;

/**
 * The default implementation of the {@link CeleritasHttpResponse}{@code <T>} interface
 *
 * @param <T> the response body type
 */
@Value
@Builder
public class CeleritasHttpResponseImpl<T> implements CeleritasHttpResponse<T> {

  private static final String READ_BODY_EXCEPTION_MESSAGE = "An error occurred while trying to read body";

  @Getter(NONE)
  CeleritasHttpClient usedClient;

  HttpResponse<T> originalResponse;
  BodyHandler<T> originalBodyHandler;
  HttpRequest originalRequest;

  @Override
  public int statusCode() {
    return originalResponse.statusCode();
  }

  @Override
  public HttpRequest request() {
    return originalResponse.request();
  }

  @Override
  public Optional<HttpResponse<T>> previousResponse() {
    return originalResponse.previousResponse();
  }

  @Override
  public HttpHeaders headers() {
    return originalResponse.headers();
  }

  @Override
  public T body() {
    return originalResponse.body();
  }

  @Override
  public Optional<SSLSession> sslSession() {
    return originalResponse.sslSession();
  }

  @Override
  public URI uri() {
    return originalResponse.uri();
  }

  @Override
  public Version version() {
    return originalResponse.version();
  }

  @Override
  public boolean is1xx() {
    return statusCode() >= 100 && statusCode() <= 199;
  }

  @Override
  public boolean is2xx() {
    return statusCode() >= 200 && statusCode() <= 299;
  }

  @Override
  public boolean isOk() {
    return statusCode() == 200;
  }

  @Override
  public boolean is3xx() {
    return statusCode() >= 300 && statusCode() <= 399;
  }

  @Override
  public boolean is4xx() {
    return statusCode() >= 400 && statusCode() <= 499;
  }

  @Override
  public boolean is5xx() {
    return statusCode() >= 500 && statusCode() <= 599;
  }

  @Override
  public HttpResponse<T> toJavaResponse() {
    return this;
  }

  @Override
  public CeleritasHttpResponse<T> retry() throws IOException, InterruptedException {
    return usedClient.send(originalRequest, originalBodyHandler);
  }

  @Override
  public Try<CeleritasHttpResponse<T>> tryRetry() {
    return usedClient.tryToSend(originalRequest, originalBodyHandler);
  }

  @Override
  public CompletableFuture<CeleritasHttpResponse<T>> retryAsync() {
    return usedClient.sendAsync(originalRequest, originalBodyHandler);
  }

  @Override
  public Future<CeleritasHttpResponse<T>> retryAsyncAsFuture() {
    return usedClient.sendAsyncAsFuture(originalRequest, originalBodyHandler);
  }

  @Override
  public <U> U readBody(@NonNull Class<U> targetClass) throws ReadBodyException {
    return tryToReadBody(targetClass)
      .getOrElseThrow(
        throwable -> new ReadBodyException(READ_BODY_EXCEPTION_MESSAGE, throwable));
  }

  @Override
  public <U> U readBody(@NonNull TypeReference<U> targetTypeReference) throws ReadBodyException {
    return tryToReadBody(targetTypeReference)
      .getOrElseThrow(
        throwable -> new ReadBodyException(READ_BODY_EXCEPTION_MESSAGE, throwable));
  }

  @Override
  public <U> Optional<U> readBodyOptional(Class<U> targetClass) {
    return tryToReadBody(targetClass).toJavaOptional();
  }

  @Override
  public <U> Optional<U> readBodyOptional(TypeReference<U> targetTypeReference) {
    return tryToReadBody(targetTypeReference).toJavaOptional();
  }

  @Override
  public <U> Option<U> readBodyOption(Class<U> targetClass) {
    return tryToReadBody(targetClass)
      .mapTry(Option::of)
      .getOrElse(API::None);
  }

  @Override
  public <U> Option<U> readBodyOption(TypeReference<U> targetTypeReference) {
    return tryToReadBody(targetTypeReference)
      .mapTry(Option::of)
      .getOrElse(API::None);
  }

  @Override
  public <U> Try<U> tryToReadBody(Class<U> targetClass) {
    return Try(() -> readBody(targetClass, null));
  }

  @Override
  public <U> Try<U> tryToReadBody(TypeReference<U> targetTypeReference) {
    return Try(() -> readBody(null, targetTypeReference));
  }

  @Override
  public <U> U readBodyForStatusCode(int code, @NonNull Class<U> targetClass) throws ReadBodyException {
    return tryToReadBodyForStatusCode(code, targetClass, null)
      .getOrElseThrow(
        throwable -> new ReadBodyException(READ_BODY_EXCEPTION_MESSAGE, throwable));
  }

  @Override
  public <U> U readBodyForStatusCode(
    int code, @NonNull TypeReference<U> targetTypeReference) throws ReadBodyException {
    return tryToReadBodyForStatusCode(code, null, targetTypeReference)
      .getOrElseThrow(
        throwable -> new ReadBodyException(READ_BODY_EXCEPTION_MESSAGE, throwable));
  }

  @Override
  public <U> Optional<U> readBodyForStatusCodeOptional(int code, Class<U> targetClass) {
    return tryToReadBodyForStatusCode(code, targetClass, null).toJavaOptional();
  }

  @Override
  public <U> Optional<U> readBodyForStatusCodeOptional(int code, TypeReference<U> targetTypeReference) {
    return tryToReadBodyForStatusCode(code, null, targetTypeReference).toJavaOptional();
  }

  @Override
  public <U> Option<U> readBodyForStatusCodeOption(int code, Class<U> targetClass) {
    return tryToReadBodyForStatusCode(code, targetClass, null)
      .mapTry(Option::of)
      .getOrElse(API::None);
  }

  @Override
  public <U> Option<U> readBodyForStatusCodeOption(int code, TypeReference<U> targetTypeReference) {
    return tryToReadBodyForStatusCode(code, null, targetTypeReference)
      .mapTry(Option::of)
      .getOrElse(API::None);
  }

  @Override
  public <U> Try<U> tryToReadBodyForStatusCode(int code, Class<U> targetClass) {
    return tryToReadBodyForStatusCode(code, targetClass, null);
  }

  @Override
  public <U> Try<U> tryToReadBodyForStatusCode(int code, TypeReference<U> targetTypeReference) {
    return tryToReadBodyForStatusCode(code, null, targetTypeReference);
  }

  private <U> Try<U> tryToReadBodyForStatusCode(int code, Class<U> clazz, TypeReference<U> tr) {
    if (statusCode() == code) {
      return Try(() -> readBody(clazz, tr));
    }
    return Failure(new ReadBodyException("The response status code does not match the given status code"));
  }

  private <U> U readBody(
    Class<U> clazz, TypeReference<U> tr) throws IOException, UnsupportedBodyTypeException, ReadBodyException {
    if (body() == null) {
      throw new ReadBodyException("The current response body is null");
    }

    val objectMapper = usedClient.getObjectMapper();

    if (body() instanceof String sBody) {
      return clazz != null
        ? objectMapper.readValue(sBody, clazz)
        : objectMapper.readValue(sBody, tr);
    } else if (body() instanceof Path pathBody) {
      val file = pathBody.toFile();
      return clazz != null
        ? objectMapper.readValue(file, clazz)
        : objectMapper.readValue(file, tr);
    } else if (body() instanceof InputStream isBody) {
      return clazz != null
        ? objectMapper.readValue(isBody, clazz)
        : objectMapper.readValue(isBody, tr);
    } else if (body() instanceof byte[] baBody) {
      return clazz != null
        ? objectMapper.readValue(baBody, clazz)
        : objectMapper.readValue(baBody, tr);
    } else if (body() instanceof Stream streamBody) {
      return clazz != null
        ? objectMapper.readValue(((Stream<String>) streamBody).collect(Collectors.joining()), clazz)
        : objectMapper.readValue(((Stream<String>) streamBody).collect(Collectors.joining()), tr);
    }

    throw new UnsupportedBodyTypeException("The current response body type is not supported");
  }
}
