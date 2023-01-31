package io.github.badpop.celeritas.http.client.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.badpop.celeritas.http.client.CeleritasHttpClient;
import io.github.badpop.celeritas.http.client.util.Value;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseBodyJsonReaderReadBodyForStatusCodeOptionalAndOptionTest {

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
  class ForOptional {
    @Nested
    class ForClass {
      @Test
      void should_read_body_for_status() throws JsonProcessingException {
        val status = 200;
        val targetClass = Value.class;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
        when(mockedObjectMapper.readValue(RESPONSE_BODY, targetClass)).thenReturn(new Value(123));

        val actual = response.readBodyForStatusCodeOptional(status, targetClass);

        assertThat(actual).contains(new Value(123));

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

        val actual = response.readBodyForStatusCodeOptional(status, targetClass);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_class() {
        val status = 200;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);

        val actual = response.readBodyForStatusCodeOptional(status, (Class<? extends Object>) null);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse, times(2)).body();
        verify(usedClient).getObjectMapper();
        verifyNoMoreInteractions(originalResponse, usedClient);
        verifyNoInteractions(originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_body() {
        val status = 200;
        val targetClass = Value.class;

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(null);

        val actual = response.readBodyForStatusCodeOptional(status, targetClass);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse).body();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
      }
    }

    @Nested
    class ForTypeReference {
      @Test
      void should_read_body_for_status() throws JsonProcessingException {
        val status = 200;
        val targetTypeReference = new TypeReference<Value>() {
        };
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
        when(mockedObjectMapper.readValue(RESPONSE_BODY, targetTypeReference)).thenReturn(new Value(123));

        val actual = response.readBodyForStatusCodeOptional(status, targetTypeReference);

        assertThat(actual).contains(new Value(123));

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

        val actual = response.readBodyForStatusCodeOptional(status, targetTypeReference);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_class() {
        val status = 200;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);

        val actual = response.readBodyForStatusCodeOptional(status, (TypeReference<? extends Object>) null);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse, times(2)).body();
        verify(usedClient).getObjectMapper();
        verifyNoMoreInteractions(originalResponse, usedClient);
        verifyNoInteractions(originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_body() {
        val status = 200;
        val targetTypeReference = new TypeReference<Value>() {
        };

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(null);

        val actual = response.readBodyForStatusCodeOptional(status, targetTypeReference);

        assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse).body();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
      }
    }
  }

  @Nested
  class ForOption {
    @Nested
    class ForClass {
      @Test
      void should_read_body_for_status() throws JsonProcessingException {
        val status = 200;
        val targetClass = Value.class;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
        when(mockedObjectMapper.readValue(RESPONSE_BODY, targetClass)).thenReturn(new Value(123));

        val actual = response.readBodyForStatusCodeOption(status, targetClass);

        VavrAssertions.assertThat(actual).contains(new Value(123));

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

        val actual = response.readBodyForStatusCodeOption(status, targetClass);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_class() {
        val status = 200;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);

        val actual = response.readBodyForStatusCodeOption(status, (Class<? extends Object>) null);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse, times(2)).body();
        verify(usedClient).getObjectMapper();
        verifyNoMoreInteractions(originalResponse, usedClient);
        verifyNoInteractions(originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_body() {
        val status = 200;
        val targetClass = Value.class;

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(null);

        val actual = response.readBodyForStatusCodeOption(status, targetClass);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse).body();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
      }
    }

    @Nested
    class ForTypeReference {
      @Test
      void should_read_body_for_status() throws JsonProcessingException {
        val status = 200;
        val targetTypeReference = new TypeReference<Value>() {
        };
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);
        when(mockedObjectMapper.readValue(RESPONSE_BODY, targetTypeReference)).thenReturn(new Value(123));

        val actual = response.readBodyForStatusCodeOption(status, targetTypeReference);

        VavrAssertions.assertThat(actual).contains(new Value(123));

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

        val actual = response.readBodyForStatusCodeOption(status, targetTypeReference);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(usedClient, mockedObjectMapper, originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_class() {
        val status = 200;
        val mockedObjectMapper = mock(ObjectMapper.class);

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(RESPONSE_BODY);
        when(usedClient.getObjectMapper()).thenReturn(mockedObjectMapper);

        val actual = response.readBodyForStatusCodeOption(status, (TypeReference<? extends Object>) null);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse, times(2)).body();
        verify(usedClient).getObjectMapper();
        verifyNoMoreInteractions(originalResponse, usedClient);
        verifyNoInteractions(originalBodyHandler, originalRequest);
      }

      @Test
      void should_not_read_body_on_null_body() {
        val status = 200;
        val targetTypeReference = new TypeReference<Value>() {
        };

        when(originalResponse.statusCode()).thenReturn(status);
        when(originalResponse.body()).thenReturn(null);

        val actual = response.readBodyForStatusCodeOption(status, targetTypeReference);

        VavrAssertions.assertThat(actual).isEmpty();

        verify(originalResponse).statusCode();
        verify(originalResponse).body();
        verifyNoMoreInteractions(originalResponse);
        verifyNoInteractions(originalBodyHandler, originalRequest, usedClient);
      }
    }
  }
}
