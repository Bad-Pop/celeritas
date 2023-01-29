package com.github.badpop.celeritas.http.client.response;

import lombok.NonNull;

import java.net.http.HttpResponse;

/**
 * CeleritasHttpResponse represents an http response.
 * This class is built on top of {@link HttpResponse} introduced in Java 11 and is fully interoperable with it.
 * But the CeleritasHttpResponse gives you access to a more complete API with many methods to simplify your work.
 *
 * <p>Finally, it gives access to methods based on the Vavr library for those who wish to develop
 * with very powerful functional interfaces.
 *
 * @param <T> the response body type
 */
public interface CeleritasHttpResponse<T> extends HttpResponse<T>, HttpResponseSendBack<T>, ResponseBodyJsonReader {

  /**
   * Check if the given http response is 1xx or not
   *
   * @return true if status code is 1xx, false otherwise
   */
  boolean is1xx();

  /**
   * Check if the given http response is 2xx or not
   *
   * @return true if status code is 2xx, false otherwise
   */
  boolean is2xx();

  /**
   * Check if the given http response is 200 OK or not
   *
   * @return true if status code is 200, false otherwise
   */
  boolean isOk();

  /**
   * Check if the given http response is 3xx or not
   *
   * @return true if status code is 3xx, false otherwise
   */
  boolean is3xx();

  /**
   * Check if the given http response is 4xx or not
   *
   * @return true if status code is 4xx, false otherwise
   */
  boolean is4xx();

  /**
   * Check if the given http response is 5xx or not
   *
   * @return true if status code is 5xx, false otherwise
   */
  boolean is5xx();

  /**
   * Transform the current CeleritasHttpResponse to a standard java {@link HttpResponse}
   *
   * @return a standard HttpResponse
   */
  HttpResponse<T> toJavaResponse();

  /**
   * Execute an action if the http response status code is 200
   *
   * @param action the action to execute
   * @return the current CeleritasHttpResponse
   * @throws NullPointerException is the given {@link Runnable} is null
   */
  default CeleritasHttpResponse<T> onOk(@NonNull Runnable action) {
    if (isOk()) {
      action.run();
    }
    return this;
  }

  /**
   * Execute an action if the http response status code is 2xx
   *
   * @param action the action to execute
   * @return the current CeleritasHttpResponse
   * @throws NullPointerException is the given {@link Runnable} is null
   */
  default CeleritasHttpResponse<T> on2xx(@NonNull Runnable action) {
    if (is2xx()) {
      action.run();
    }
    return this;
  }

  /**
   * Execute an action if the http response status code is not 2xx
   *
   * @param action the action to execute
   * @return the current CeleritasHttpResponse
   * @throws NullPointerException is the given {@link Runnable} is null
   */
  default CeleritasHttpResponse<T> onKo(@NonNull Runnable action) {
    if (!is2xx()) {
      action.run();
    }
    return this;
  }

  /**
   * Execute an action
   *
   * @param action the action to execute
   * @return the current CeleritasHttpResponse
   * @throws NullPointerException is the given {@link Runnable} is null
   */
  default CeleritasHttpResponse<T> andThen(@NonNull Runnable action) {
    action.run();
    return this;
  }
}
