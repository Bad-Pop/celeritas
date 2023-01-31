package io.github.badpop.celeritas.http.client.response;

import io.github.badpop.celeritas.http.client.CeleritasHttpClient;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CeleritasHttpResponseImplTest {

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
  void should_get_status_code() {
    val status = 200;

    when(originalResponse.statusCode()).thenReturn(status);

    val actual = response.statusCode();

    assertThat(actual).isEqualTo(status);

    verify(originalResponse).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_request() {
    when(originalResponse.request()).thenReturn(originalRequest);

    val actual = response.request();

    assertThat(actual).isEqualTo(originalRequest);

    verify(originalResponse).request();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_previous_response() {
    when(originalResponse.previousResponse()).thenReturn(Optional.of(response));

    val actual = response.previousResponse();

    assertThat(actual).contains(response);

    verify(originalResponse).previousResponse();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_headers() {
    val headers = mock(HttpHeaders.class);
    when(originalResponse.headers()).thenReturn(headers);

    val actual = response.headers();

    assertThat(actual).isEqualTo(headers);

    verify(originalResponse).headers();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_body() {
    val body = "body";
    when(originalResponse.body()).thenReturn(body);

    val actual = response.body();

    assertThat(actual).isEqualTo(body);

    verify(originalResponse).body();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_ssl_session() {
    val sslSession = mock(SSLSession.class);
    when(originalResponse.sslSession()).thenReturn(Optional.of(sslSession));

    val actual = response.sslSession();

    assertThat(actual).contains(sslSession);

    verify(originalResponse).sslSession();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_URI() {
    val uri = URI.create("http://localhost");
    when(originalResponse.uri()).thenReturn(uri);

    val actual = response.uri();

    assertThat(actual).isEqualTo(uri);

    verify(originalResponse).uri();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_get_version() {
    val version = HTTP_1_1;
    when(originalResponse.version()).thenReturn(version);

    val actual = response.version();

    assertThat(actual).isEqualTo(version);

    verify(originalResponse).version();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }
}
