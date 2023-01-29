package com.github.badpop.celeritas.http.client;

import com.github.badpop.celeritas.extension.MockServerExtension;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpError;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletionException;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
class CeleritasHttpClientAsyncFutureSenderTest {

  private final CeleritasHttpClient client = CeleritasHttpClient.newClient();

  @Test
  void should_send_request(String host, Integer port, ClientAndServer mockServer) {
    val path = "/path";
    val request = HttpRequest.newBuilder()
      .GET()
      .uri(URI.create(String.format("%s:%s%s", host, port, path)))
      .build();
    val bodyHandler = client.createDiscardingBodyHandler();

    val mockRequest = request().withMethod("GET").withPath(path);
    mockServer
      .when(mockRequest)
      .respond(
        response().withStatusCode(200));

    val actual = client.sendAsyncAsFuture(request, bodyHandler).await();

    assertThat(actual.isSuccess()).isTrue();
    assertThat(actual.get().statusCode()).isEqualTo(200);
    mockServer.verify(mockRequest);
  }

  @Test
  void should_fail_to_send_request(String host, Integer port, ClientAndServer mockServer) {
    val path = "/path";
    val request = HttpRequest.newBuilder()
      .GET()
      .uri(URI.create(String.format("%s:%s%s", host, port, path)))
      .build();
    val bodyHandler = HttpResponse.BodyHandlers.discarding();

    val mockRequest = request().withMethod("GET").withPath(path);
    mockServer
      .when(mockRequest)
      .error(HttpError.error().withDropConnection(TRUE));

    val actual = client.sendAsyncAsFuture(request, bodyHandler).await();

    assertThat(actual.isFailure()).isTrue();
    VavrAssertions.assertThat(actual.getCause()).containsInstanceOf(CompletionException.class);

    mockServer.verify(mockRequest);
  }
}
