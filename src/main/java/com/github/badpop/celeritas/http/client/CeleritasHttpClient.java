package com.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.celeritas.http.client.response.CeleritasHttpResponse;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import lombok.NonNull;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.util.concurrent.CompletableFuture;

/**
 * CeleritasHttpClient is an http client built on top of the native {@link HttpClient} introduced in Java 11.
 * This client is fully based on the java 11 client but gives you access to a more complete API with many methods to simplify your work.
 * It also relies on the Jackson library and its famous {@link ObjectMapper} to provide serialization and deserialization features directly from the client.
 * Finally, it gives access to methods based on the Vavr library for those who wish to develop with very powerful functional interfaces.
 */
public interface CeleritasHttpClient extends HttpRequestBodyPublisherProvider, HttpResponseBodyHandlerProvider {

  /**
   * Builds a new instance of {@link CeleritasHttpClient} with a default {@link HttpClient} and {@link ObjectMapper}.
   *
   * @return a new Celeritas http client
   */
  static CeleritasHttpClient newClient() {
    return new CeleritasHttpClientImpl();
  }

  /**
   * Builds a new instance of {@link CeleritasHttpClient} with the given {@link HttpClient} and a default {@link ObjectMapper}.
   *
   * @return a new Celeritas http client
   * @throws NullPointerException if the given HttpClient is null
   */
  static CeleritasHttpClient newClient(@NonNull HttpClient httpClient) {
    return new CeleritasHttpClientImpl(httpClient);
  }

  /**
   * Builds a new instance of {@link CeleritasHttpClient} with a default {@link HttpClient} and the given {@link ObjectMapper}.
   *
   * @return a new Celeritas http client
   * @throws NullPointerException if the given ObjectMapper is null
   */
  static CeleritasHttpClient newClient(@NonNull ObjectMapper objectMapper) {
    return new CeleritasHttpClientImpl(objectMapper);
  }

  /**
   * Builds a new instance of {@link CeleritasHttpClient} with the given {@link HttpClient} and {@link ObjectMapper}.
   *
   * @return a new Celeritas http client
   * @throws NullPointerException if one of the given params is null
   */
  static CeleritasHttpClient newClient(@NonNull HttpClient httpClient, @NonNull ObjectMapper objectMapper) {
    return new CeleritasHttpClientImpl(httpClient, objectMapper);
  }

  /**
   * @return the underlying {@link HttpClient} that the Celeritas client works with
   */
  HttpClient getHttpClient();

  /**
   * @return the underlying {@link ObjectMapper} that the Celeritas client works with
   */
  ObjectMapper getObjectMapper();

  /**
   * Sends the given request using this client, blocking if necessary to get the response.
   * The returned {@link CeleritasHttpResponse}{@code <T>} contains the response status, headers, and body ( as handled by given response body handler ).
   *
   * <p>For more details, take a look at {@link HttpClient#send(HttpRequest, BodyHandler)}.
   *
   * @param request             the http request to send
   * @param responseBodyHandler the BodyHandler to apply on the response body
   * @param <T>                 the response body type
   * @return the http response
   * @throws IOException              if an I/O error occurs when sending or receiving
   * @throws InterruptedException     if the operation is interrupted
   * @throws IllegalArgumentException if the {@code request} argument is not
   *                                  a request that could have been validly built as specified by {@link
   *                                  HttpRequest.Builder}.
   * @throws SecurityException        If a security manager has been installed,
   *                                  and it denies {@link java.net.URLPermission access} to the
   *                                  URL in the given request, or proxy if one is configured.
   *                                  See {@link HttpClient#send(HttpRequest, BodyHandler)} for further information.
   * @throws NullPointerException     if request or responseBodyHandler is null
   */
  <T> CeleritasHttpResponse<T> send(@NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler)
    throws IOException, IllegalArgumentException, InterruptedException, SecurityException;

  /**
   * Same as {@link CeleritasHttpClient#send(HttpRequest, BodyHandler)} but wraps potential errors in a Vavr functional {@link Try}.
   *
   * @param request             the http request to send
   * @param responseBodyHandler the BodyHandler to apply on the response body
   * @param <T>                 the response body type
   * @return A functional {@link Try} containing either a {@link Throwable} or the http response
   */
  <T> Try<CeleritasHttpResponse<T>> tryToSend(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler);

  /**
   * Sends the given request asynchronously using this client with the given response body handler.
   *
   * <p>For more details, take a look at {@link HttpClient#sendAsync(HttpRequest, BodyHandler)}.
   *
   * @param request             the http request to send
   * @param responseBodyHandler the BodyHandler to apply on the response body
   * @param <T>                 the response body type
   * @return a {@code CompletableFuture<CeleritasHttpResponse<T>>}
   * @throws IllegalArgumentException if the {@code request} argument is not a request that could
   *                                  have been validly built as specified by {@link HttpRequest.Builder}.
   * @throws NullPointerException     if request or responseBodyHandler is null
   */
  <T> CompletableFuture<CeleritasHttpResponse<T>> sendAsync(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler);

  /**
   * Same as {@link #sendAsync(HttpRequest, BodyHandler)}
   * but use a Vavr functional {@link Future} instead of a {@link CompletableFuture}
   *
   * @param request             the http request to send
   * @param responseBodyHandler the BodyHandler to apply on the response body
   * @param <T>                 the response body type
   * @return a {@code Future<CeleritasHttpResponse<T>>}
   */
  <T> Future<CeleritasHttpResponse<T>> sendAsyncAsFuture(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler);
}
