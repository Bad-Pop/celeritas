package io.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.github.badpop.celeritas.http.client.exception.JsonBodyPublisherCreationException;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpRequestBodyPublisherProviderTest {

  @InjectMocks
  private CeleritasHttpClientImpl bodyPublisherProvider;

  @Mock
  private HttpClient httpClient;

  @Mock
  private ObjectMapper objectMapper;

  @Nested
  class CreateJsonBodyPublisher {

    @Test
    void should_create() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();
      val json = "{\"property\": 1234}";

      when(objectMapper.canSerialize(body.getClass())).thenReturn(true);
      when(objectMapper.writeValueAsString(body)).thenReturn(json);

      val actual = bodyPublisherProvider.createJsonBodyPublisher(body);

      assertThat(actual.contentLength()).isEqualTo(json.length());

      verify(objectMapper).canSerialize(body.getClass());
      verify(objectMapper).writeValueAsString(body);
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }

    @Test
    void should_not_create_if_not_serializable() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();

      when(objectMapper.canSerialize(body.getClass())).thenReturn(false);

      assertThatExceptionOfType(JsonBodyPublisherCreationException.class)
        .isThrownBy(() -> bodyPublisherProvider.createJsonBodyPublisher(body));

      verify(objectMapper).canSerialize(body.getClass());
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }

    @Test
    void should_not_create_on_serialization_error() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();
      val ex = new InvalidFormatException(null, "error", null, null);

      when(objectMapper.canSerialize(body.getClass())).thenReturn(true);
      when(objectMapper.writeValueAsString(body)).thenThrow(ex);

      assertThatExceptionOfType(JsonBodyPublisherCreationException.class)
        .isThrownBy(() -> bodyPublisherProvider.createJsonBodyPublisher(body));

      verify(objectMapper).canSerialize(body.getClass());
      verify(objectMapper).writeValueAsString(body);
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }

    @Test
    void should_throw_npe_on_null_body() {
      assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> bodyPublisherProvider.createJsonBodyPublisher(null));
      verifyNoInteractions(objectMapper, httpClient);
    }
  }

  @Nested
  class TryToCreateJsonBodyPublisher {

    @Test
    void should_create() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();
      val json = "{\"property\": 1234}";

      when(objectMapper.canSerialize(body.getClass())).thenReturn(true);
      when(objectMapper.writeValueAsString(body)).thenReturn(json);

      val actual = bodyPublisherProvider.tryToCreateJsonBodyPublisher(body);

      VavrAssertions.assertThat(actual).containsInstanceOf(BodyPublisher.class);
      assertThat(actual.get().contentLength()).isEqualTo(json.length());

      verify(objectMapper).canSerialize(body.getClass());
      verify(objectMapper).writeValueAsString(body);
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }

    @Test
    void should_not_create_if_not_serializable() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();
      val json = "{\"property\": 1234}";

      when(objectMapper.canSerialize(body.getClass())).thenReturn(false);

      val actual = bodyPublisherProvider.tryToCreateJsonBodyPublisher(body);

      VavrAssertions.assertThat(actual).failBecauseOf(JsonBodyPublisherCreationException.class)
        .failReasonHasMessage("Unable to create body publisher, unsupported body type");

      verify(objectMapper).canSerialize(body.getClass());
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }

    @Test
    void should_not_create_on_serialization_error() throws JsonProcessingException, JsonBodyPublisherCreationException {
      val body = new Object();
      val ex = new InvalidFormatException(null, "error", null, null);

      when(objectMapper.canSerialize(body.getClass())).thenReturn(true);
      when(objectMapper.writeValueAsString(body)).thenThrow(ex);

      val actual = bodyPublisherProvider.tryToCreateJsonBodyPublisher(body);

      VavrAssertions.assertThat(actual).failBecauseOf(InvalidFormatException.class);

      verify(objectMapper).canSerialize(body.getClass());
      verify(objectMapper).writeValueAsString(body);
      verifyNoMoreInteractions(objectMapper);
      verifyNoInteractions(httpClient);
    }
  }

  @Test
  void should_create_no_body_publisher() {
    val actual = bodyPublisherProvider.createNoBodyBodyPublisher();
    assertThat(actual).isNotNull();
  }

  @Test
  void should_not_create_publisher_on_null_body() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyPublisherProvider.createStringBodyPublisher(null));
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyPublisherProvider.createInputStreamBodyPublisher(null));
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyPublisherProvider.createByteArrayBodyPublisher(null));
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyPublisherProvider.createFileBodyPublisher(null));
  }
}
