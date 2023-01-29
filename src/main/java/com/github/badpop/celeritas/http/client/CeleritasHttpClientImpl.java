package com.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.celeritas.http.client.exception.JsonBodyPublisherCreationException;
import com.github.badpop.celeritas.http.client.response.CeleritasHttpResponse;
import com.github.badpop.celeritas.http.client.response.CeleritasHttpResponseImpl;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;
import lombok.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.concurrent.CompletableFuture;

import static io.vavr.API.Failure;
import static io.vavr.API.Try;

/**
 * The default implementation of the {@link CeleritasHttpClient} interface
 */
@Value
@Builder
public class CeleritasHttpClientImpl implements CeleritasHttpClient {

  @With
  HttpClient httpClient;

  @With
  ObjectMapper objectMapper;

  public CeleritasHttpClientImpl() {
    this.httpClient = defaultHttpClient();
    this.objectMapper = defaultObjectMapper();
  }

  public CeleritasHttpClientImpl(HttpClient httpClient) {
    this.httpClient = httpClient;
    this.objectMapper = defaultObjectMapper();
  }

  public CeleritasHttpClientImpl(ObjectMapper objectMapper) {
    this.httpClient = defaultHttpClient();
    this.objectMapper = objectMapper;
  }

  public CeleritasHttpClientImpl(HttpClient httpClient, ObjectMapper objectMapper) {
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> CeleritasHttpResponse<T> send(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler)
    throws IOException, InterruptedException {
    return executeAndWrapResponse(request, responseBodyHandler);
  }

  @Override
  public <T> Try<CeleritasHttpResponse<T>> tryToSend(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler) {
    return Try(() -> executeAndWrapResponse(request, responseBodyHandler));
  }

  @Override
  public <T> CompletableFuture<CeleritasHttpResponse<T>> sendAsync(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler) {
    return executeAsyncAndWrapResponse(request, responseBodyHandler);
  }

  @Override
  public <T> Future<CeleritasHttpResponse<T>> sendAsyncAsFuture(
    @NonNull HttpRequest request, @NonNull BodyHandler<T> responseBodyHandler) {
    return executeAsyncFutureAndWrapResponse(request, responseBodyHandler);
  }

  @Override
  public <U> BodyPublisher createJsonBodyPublisher(@NonNull U body) throws JsonBodyPublisherCreationException {
    return tryToCreateJsonBodyPublisher(body)
      .getOrElseThrow(throwable ->
        new JsonBodyPublisherCreationException(
          "An error occurred while trying to serialize body", throwable));
  }

  @Override
  public <U> Try<BodyPublisher> tryToCreateJsonBodyPublisher(U body) {
    if (objectMapper.canSerialize(body.getClass())) {
      return Try(() -> objectMapper.writeValueAsString(body))
        .mapTry(BodyPublishers::ofString);
    }

    return Failure(new JsonBodyPublisherCreationException("Unable to create body publisher, unsupported body type"));
  }

  private HttpClient defaultHttpClient() {
    return HttpClient.newHttpClient();
  }

  private ObjectMapper defaultObjectMapper() {
    return new ObjectMapper().registerModules(new VavrModule(), new JavaTimeModule());
  }

  private <T> CeleritasHttpResponse<T> executeAndWrapResponse(
    HttpRequest httpRequest, BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
    val jdkResponse = httpClient.send(httpRequest, bodyHandler);
    return wrapResponse(jdkResponse, bodyHandler, httpRequest);
  }

  private <T> CompletableFuture<CeleritasHttpResponse<T>> executeAsyncAndWrapResponse(
    HttpRequest httpRequest, BodyHandler<T> bodyHandler) {
    return httpClient.sendAsync(httpRequest, bodyHandler)
      .thenApply(jdkResponse -> wrapResponse(jdkResponse, bodyHandler, httpRequest));
  }

  private <T> Future<CeleritasHttpResponse<T>> executeAsyncFutureAndWrapResponse(
    HttpRequest httpRequest, BodyHandler<T> bodyHandler) {
    return Future.fromCompletableFuture(httpClient.sendAsync(httpRequest, bodyHandler))
      .map(jdkResponse -> wrapResponse(jdkResponse, bodyHandler, httpRequest));
  }

  private <T> CeleritasHttpResponse<T> wrapResponse(
    HttpResponse<T> response, BodyHandler<T> bodyHandler, HttpRequest httpRequest) {

    return CeleritasHttpResponseImpl.<T>builder()
      .usedClient(this)
      .originalResponse(response)
      .originalBodyHandler(bodyHandler)
      .originalRequest(httpRequest)
      .build();
  }
}
