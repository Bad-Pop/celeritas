package com.github.badpop.celeritas.http.client.response;

import com.github.badpop.celeritas.http.client.CeleritasHttpClient;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.util.concurrent.CompletableFuture;

/**
 * Utility interface to replay the request that generated the current response
 *
 * @param <T> the response body type
 */
public interface HttpResponseSendBack<T> {

  /**
   * Send back the request that generated this response with the used http client.
   *
   * <p>For more details take a look at
   * {@link CeleritasHttpClient#send(HttpRequest, BodyHandler)}
   *
   * @return a new http response
   * @throws IOException          if an I/O error occurs when sending or receiving
   * @throws InterruptedException if the operation is interrupted
   * @throws SecurityException    If a security manager has been installed
   *                              and it denies {@link java.net.URLPermission access} to the
   *                              URL in the given request, or proxy if one is configured.
   *                              See {@link HttpClient#send(HttpRequest, BodyHandler)} for further information.
   */
  CeleritasHttpResponse<T> retry() throws IOException, InterruptedException;

  /**
   * Same as {@link #retry()} but wraps potential errors in a Vavr functional {@link Try}
   *
   * <p>For more details take a look at
   * {@link CeleritasHttpClient#tryToSend(HttpRequest, BodyHandler)}
   *
   * @return a functional {@link Try} containing either a {@link Throwable} or the response
   */
  Try<CeleritasHttpResponse<T>> tryRetry();

  /**
   * Asynchronously send back the request that generated this response with the used http client.
   *
   * <p>For more details, take a look at {@link CeleritasHttpClient#sendAsync(HttpRequest, BodyHandler)}.
   *
   * @return a {@code CompletableFuture<CeleritasHttpResponse<T>>}
   */
  CompletableFuture<CeleritasHttpResponse<T>> retryAsync();

  /**
   * Same as {@link #retryAsync()} but use a Vavr functional {@link Future} instead of a {@link CompletableFuture}.
   *
   * <p>For more details, take a look at {@link CeleritasHttpClient#sendAsyncAsFuture(HttpRequest, BodyHandler)}.
   *
   * @return a {@code Future<CeleritasHttpResponse<T>>}
   */
  Future<CeleritasHttpResponse<T>> retryAsyncAsFuture();
}
