package com.github.badpop.celeritas.http.client.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.celeritas.http.client.CeleritasHttpClient;
import com.github.badpop.celeritas.http.client.exception.ReadBodyException;
import com.github.badpop.celeritas.http.client.util.Value;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseBodyJsonReaderReadBodyForStatusCodeTest {

  @InjectMocks
  private CeleritasHttpResponseImpl<String> response;

  @Mock
  private CeleritasHttpClient usedClient;

  @Mock
  private HttpResponse<String> originalResponse;

  @Mock
  private BodyHandler<String> originalBodyHandler;

  @Mock
  private HttpRequest originalRequest;

  private static final String RESPONSE_BODY = """
    {
        "value": 123
    }
    """;

  @Nested
  class ForClass {
    @Test
    void should_read_body_for_status() throws ReadBodyException, JsonProcessingException {
      val status = 200;
      val targetClass = Value.class;
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.statusCode()).thenReturn(status);
      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetClass)).thenReturn(new Value(123));

      val actual = response.readBodyForStatusCode(status, targetClass);

      assertThat(actual).isEqualTo(new Value(123));

      verify(originalResponse).statusCode();
      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetClass);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_for_status_if_statuses_are_not_equals() {
      val status = 200;
      val responseStatus = 400;
      val targetClass = Value.class;
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.statusCode()).thenReturn(responseStatus);

      assertThatExceptionOfType(ReadBodyException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, targetClass));

      verify(originalResponse).statusCode();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() {
      val status = 200;

      assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, (Class<? extends Object>) null));

      verifyNoInteractions(originalResponse, usedClient, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val status = 200;
      val targetClass = Value.class;

      when(originalResponse.statusCode()).thenReturn(status);
      when(originalResponse.body()).thenReturn(null);

      assertThatExceptionOfType(ReadBodyException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, targetClass));

      verify(originalResponse).statusCode();
      verify(originalResponse).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
    }
  }

  @Nested
  class ForTypeReference {
    @Test
    void should_read_body_for_status() throws ReadBodyException, JsonProcessingException {
      val status = 200;
      val targetTypeReference = new TypeReference<Value>() {
      };
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.statusCode()).thenReturn(status);
      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetTypeReference)).thenReturn(new Value(123));

      val actual = response.readBodyForStatusCode(status, targetTypeReference);

      assertThat(actual).isEqualTo(new Value(123));

      verify(originalResponse).statusCode();
      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetTypeReference);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_for_status_if_statuses_are_not_equals() {
      val status = 200;
      val responseStatus = 400;
      val targetTypeReference = new TypeReference<Value>() {
      };
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.statusCode()).thenReturn(responseStatus);

      assertThatExceptionOfType(ReadBodyException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, targetTypeReference));

      verify(originalResponse).statusCode();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() {
      val status = 200;

      assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, (TypeReference<? extends Object>) null));

      verifyNoInteractions(originalResponse, usedClient, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val status = 200;
      val targetTypeReference = new TypeReference<Value>() {
      };

      when(originalResponse.statusCode()).thenReturn(status);
      when(originalResponse.body()).thenReturn(null);

      assertThatExceptionOfType(ReadBodyException.class)
        .isThrownBy(() -> response.readBodyForStatusCode(status, targetTypeReference));

      verify(originalResponse).statusCode();
      verify(originalResponse).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
    }
  }
}
