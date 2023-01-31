package io.github.badpop.celeritas.http.client.response;

import io.github.badpop.celeritas.http.client.CeleritasHttpClient;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CeleritasHttpResponseTest {

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

  @ParameterizedTest
  @CsvSource({
    "100,true,2",
    "199,true,2",
    "99,false,1",
    "200,false,2",
  })
  void is1xx(int status, boolean shouldBe, int expectedTime) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.is1xx()).isEqualTo(shouldBe);

    verify(originalResponse, times(expectedTime)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @ParameterizedTest
  @CsvSource({
    "200,true,2",
    "299,true,2",
    "199,false,1",
    "300,false,2",
  })
  void is2xx(int status, boolean shouldBe, int expectedTime) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.is2xx()).isEqualTo(shouldBe);

    verify(originalResponse, times(expectedTime)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @ParameterizedTest
  @CsvSource({
    "200,true",
    "199,false",
    "201,false",
  })
  void isOk(int status, boolean shouldBe) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.isOk()).isEqualTo(shouldBe);

    verify(originalResponse).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @ParameterizedTest
  @CsvSource({
    "300,true,2",
    "399,true,2",
    "299,false,1",
    "400,false,2",
  })
  void is3xx(int status, boolean shouldBe, int expectedTime) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.is3xx()).isEqualTo(shouldBe);

    verify(originalResponse, times(expectedTime)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @ParameterizedTest
  @CsvSource({
    "400,true,2",
    "499,true,2",
    "399,false,1",
    "500,false,2",
  })
  void is4xx(int status, boolean shouldBe, int expectedTime) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.is4xx()).isEqualTo(shouldBe);

    verify(originalResponse, times(expectedTime)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @ParameterizedTest
  @CsvSource({
    "500,true,2",
    "599,true,2",
    "499,false,1",
    "600,false,2",
  })
  void is5xx(int status, boolean shouldBe, int expectedTime) {
    when(originalResponse.statusCode()).thenReturn(status);

    assertThat(response.is5xx()).isEqualTo(shouldBe);

    verify(originalResponse, times(expectedTime)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void to_java_response() {
    val actual = response.toJavaResponse();
    assertThat(actual).isEqualTo(response);
  }

  @Test
  void should_run_on_ok() {
    val mock = mock(CeleritasHttpResponse.class);

    when(mock.toJavaResponse()).thenReturn(response);
    when(originalResponse.statusCode()).thenReturn(200);

    val actual = response.onOk(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(mock).toJavaResponse();
    verify(originalResponse).statusCode();
    verifyNoMoreInteractions(mock, originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_ok() {
    val mock = mock(CeleritasHttpResponse.class);

    when(originalResponse.statusCode()).thenReturn(300);

    val actual = response.onOk(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(originalResponse).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(mock, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_ok_with_null_runnable() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> response.onOk(null));

    verifyNoInteractions(originalResponse, originalResponse, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_run_on_2xx() {
    val mock = mock(CeleritasHttpResponse.class);

    when(mock.toJavaResponse()).thenReturn(response);
    when(originalResponse.statusCode()).thenReturn(201);

    val actual = response.on2xx(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(mock).toJavaResponse();
    verify(originalResponse, times(2)).statusCode();
    verifyNoMoreInteractions(mock, originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_2xx() {
    val mock = mock(CeleritasHttpResponse.class);

    when(originalResponse.statusCode()).thenReturn(300);

    val actual = response.on2xx(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(originalResponse, times(2)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(mock, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_2xx_with_null_runnable() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> response.on2xx(null));

    verifyNoInteractions(originalResponse, originalResponse, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_run_on_ko() {
    val mock = mock(CeleritasHttpResponse.class);

    when(mock.toJavaResponse()).thenReturn(response);
    when(originalResponse.statusCode()).thenReturn(500);

    val actual = response.onKo(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(mock).toJavaResponse();
    verify(originalResponse, times(2)).statusCode();
    verifyNoMoreInteractions(mock, originalResponse);
    verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_ko() {
    val mock = mock(CeleritasHttpResponse.class);

    when(originalResponse.statusCode()).thenReturn(200);

    val actual = response.onKo(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(originalResponse, times(2)).statusCode();
    verifyNoMoreInteractions(originalResponse);
    verifyNoInteractions(mock, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_ko_with_null_runnable() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> response.onKo(null));

    verifyNoInteractions(originalResponse, originalResponse, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_run_on_and_then() {
    val mock = mock(CeleritasHttpResponse.class);

    when(mock.toJavaResponse()).thenReturn(response);

    val actual = response.andThen(mock::toJavaResponse);
    assertThat(actual).isEqualTo(response);

    verify(mock).toJavaResponse();
    verifyNoInteractions(originalResponse, originalResponse, usedClient, originalBodyHandler, originalRequest);
  }

  @Test
  void should_not_run_on_and_then_null_runnable() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> response.andThen(null));

    verifyNoInteractions(originalResponse, originalResponse, usedClient, originalBodyHandler, originalRequest);
  }
}
