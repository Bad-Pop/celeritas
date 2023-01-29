package com.github.badpop.celeritas.http.client.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.badpop.celeritas.http.client.CeleritasHttpClient;
import com.github.badpop.celeritas.http.client.exception.ReadBodyException;
import com.github.badpop.celeritas.http.client.util.Value;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseBodyJsonReaderTryReadToBodyTest {

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

  private static final String RESPONSE_BODY = """
    {
        "value": 123
    }
    """;

  @Nested
  class ForClass {
    @Test
    void should_read_body() throws JsonProcessingException {
      val targetClass = Value.class;
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetClass)).thenReturn(new Value(123));

      val actual = response.tryToReadBody(targetClass);

      VavrAssertions.assertThat(actual).contains(new Value(123));

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetClass);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() throws JsonProcessingException {
      val mockedObjectMapper = mock(ObjectMapper.class);
      val ex = new InvalidFormatException(null, "error", null, null);

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, (TypeReference<? extends Object>) null)).thenThrow(ex);

      val actual = response.tryToReadBody((Class<? extends Object>) null);

      VavrAssertions.assertThat(actual).failBecauseOf(InvalidFormatException.class);

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, (TypeReference<? extends Object>) null);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val targetClass = Value.class;

      when(originalResponse.body()).thenReturn(null);

      val actual = response.tryToReadBody(targetClass);

      VavrAssertions.assertThat(actual).failBecauseOf(ReadBodyException.class);

      verify(originalResponse, times(1)).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
    }
  }

  @Nested
  class ForTypeReference {
    @Test
    void should_read_body() throws JsonProcessingException {
      val targetTypeReference = new TypeReference<Value>() {
      };
      val mockedObjectMapper = mock(ObjectMapper.class);

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, targetTypeReference)).thenReturn(new Value(123));

      val actual = response.tryToReadBody(targetTypeReference);

      VavrAssertions.assertThat(actual).contains(new Value(123));

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, targetTypeReference);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_class() throws JsonProcessingException {
      val mockedObjectMapper = mock(ObjectMapper.class);
      val ex = new InvalidFormatException(null, "error", null, null);

      when(originalResponse.body()).thenReturn(RESPONSE_BODY);
      when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
      when(mockedObjectMapper.readValue(RESPONSE_BODY, (TypeReference<? extends Object>) null)).thenThrow(ex);

      val actual = response.tryToReadBody((TypeReference<? extends Object>) null);

      VavrAssertions.assertThat(actual).failBecauseOf(InvalidFormatException.class);

      verify(originalResponse, times(2)).body();
      verify(usedClient).getObjectMapper();
      verify(mockedObjectMapper).readValue(RESPONSE_BODY, (TypeReference<? extends Object>) null);
      verifyNoMoreInteractions(originalResponse, usedClient, mockedObjectMapper);
      verifyNoInteractions(originalBodyHandler, originalRequest);
    }

    @Test
    void should_not_read_body_on_null_body() {
      val targetTypeReference = new TypeReference<Value>() {
      };

      when(originalResponse.body()).thenReturn(null);

      val actual = response.tryToReadBody(targetTypeReference);

      VavrAssertions.assertThat(actual).failBecauseOf(ReadBodyException.class);

      verify(originalResponse, times(1)).body();
      verifyNoMoreInteractions(originalResponse);
      verifyNoInteractions(usedClient, originalBodyHandler, originalRequest);
    }
  }
}
