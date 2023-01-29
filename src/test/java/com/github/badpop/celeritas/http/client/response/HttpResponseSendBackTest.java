package com.github.badpop.celeritas.http.client.response;

import com.github.badpop.celeritas.http.client.CeleritasHttpClient;
import io.vavr.concurrent.Future;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.vavr.API.Success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpResponseSendBackTest {

  @InjectMocks
  private CeleritasHttpResponseImpl<String> response;

  @Mock
  private CeleritasHttpClient usedClient;

  @Mock
  private HttpResponse<String> originalResponse;

  @Mock
  private HttpResponse.BodyHandler<String> originalBodyHandler;

  @Mock
  private HttpRequest originalRequest;

  @Test
  void should_retry() throws IOException, InterruptedException {
    when(usedClient.send(originalRequest, originalBodyHandler)).thenReturn(response);

    val actual = response.retry();

    assertThat(actual).isEqualTo(response);

    verify(usedClient).send(originalRequest, originalBodyHandler);
    verifyNoMoreInteractions(usedClient);
    verifyNoInteractions(originalResponse, originalRequest, originalBodyHandler);
  }

  @Test
  void should_try_retry() {
    when(usedClient.tryToSend(originalRequest, originalBodyHandler)).thenReturn(Success(response));

    val actual = response.tryRetry();

    VavrAssertions.assertThat(actual).containsSame(response);

    verify(usedClient).tryToSend(originalRequest, originalBodyHandler);
    verifyNoMoreInteractions(usedClient);
    verifyNoInteractions(originalResponse, originalRequest, originalBodyHandler);
  }

  @Test
  void should_retry_async() throws InterruptedException, ExecutionException {
    when(usedClient.sendAsync(originalRequest, originalBodyHandler)).thenReturn(CompletableFuture.completedFuture(response));

    val actual = response.retryAsync();

    assertThat(actual).isCompleted();
    assertThat(actual.get()).isEqualTo(response);

    verify(usedClient).sendAsync(originalRequest, originalBodyHandler);
    verifyNoMoreInteractions(usedClient);
    verifyNoInteractions(originalResponse, originalRequest, originalBodyHandler);
  }

  @Test
  void should_retry_async_future() {
    when(usedClient.sendAsyncAsFuture(originalRequest, originalBodyHandler)).thenReturn(Future.successful(response));

    val actual = response.retryAsyncAsFuture();

    assertThat(actual.isCompleted()).isTrue();
    assertThat(actual.isSuccess()).isTrue();
    assertThat(actual.get()).isEqualTo(response);

    verify(usedClient).sendAsyncAsFuture(originalRequest, originalBodyHandler);
    verifyNoMoreInteractions(usedClient);
    verifyNoInteractions(originalResponse, originalRequest, originalBodyHandler);
  }
}
