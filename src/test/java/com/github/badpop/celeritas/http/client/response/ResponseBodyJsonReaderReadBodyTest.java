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
class ResponseBodyJsonReaderReadBodyTest {

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
    void should_read_body() throws ReadBodyException, JsonProcessingException {
      val targetClass = Value.class;
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetClass)).thenReturn(new Value(123));

      val actual = response.readBody(targetClass);

      assertThat(actual).isEqualTo(new Value(123));

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetClass);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() {
      assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> response.readBody((Class<? extends Object>) null));

      verifyNoInteractions(originalResponse, usedClient, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val targetClass = Value.class;

      when(originalResponse.body()).thenReturn(null);

      assertThatExceptionOfType(ReadBodyException.class).isThrownBy(() -> response.readBody(targetClass));

      verify(originalResponse, times(1)).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
    }
  }

  @Nested
  class ForTypeReference {
    @Test
    void should_read_body() throws ReadBodyException, JsonProcessingException {
      val mockedObjectMapper = mock(ObjectMapper.class);
      val targetTypeReference = new TypeReference<Value>() {
      };

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetTypeReference)).thenReturn(new Value(123));

      val actual = response.readBody(targetTypeReference);

      assertThat(actual).isEqualTo(new Value(123));

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetTypeReference);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() {
      assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> response.readBody((TypeReference<? extends Object>) null));

      verifyNoInteractions(originalResponse, usedClient, originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val targetClass = new TypeReference<Value>() {
      };

      when(originalResponse.body()).thenReturn(null);

      assertThatExceptionOfType(ReadBodyException.class).isThrownBy(() -> response.readBody(targetClass));

      verify(originalResponse, times(1)).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
    }
  }
}
