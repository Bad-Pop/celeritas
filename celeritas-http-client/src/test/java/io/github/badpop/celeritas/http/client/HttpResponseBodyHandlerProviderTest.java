package io.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class HttpResponseBodyHandlerProviderTest {

  @InjectMocks
  private CeleritasHttpClientImpl bodyHandlerProvider;

  @Mock
  private HttpClient httpClient;

  @Mock
  private ObjectMapper objectMapper;

  @Test
  void should_create_discarding_handler() {
    val actual = bodyHandlerProvider.createDiscardingBodyHandler();
    assertThat(actual)
      .usingRecursiveComparison()
      .isEqualTo(BodyHandlers.discarding());
  }

  @Test
  void should_create_byte_array_handler() {
    val actual = bodyHandlerProvider.createByteArrayingBodyHandler();
    assertThat(actual)
      .usingRecursiveComparison()
      .isEqualTo(BodyHandlers.ofByteArray());
  }

  @Test
  void should_create_string_handler() {
    val actual = bodyHandlerProvider.createStringBodyHandler();
    assertThat(actual)
      .usingRecursiveComparison()
      .isEqualTo(BodyHandlers.ofString());
  }

  @Test
  void should_not_create_string_handler_with_null_charset() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyHandlerProvider.createStringBodyHandler(null));
  }

  @Test
  void should_not_create_file_handler() {
    assertThatExceptionOfType(NullPointerException.class)
      .isThrownBy(() -> bodyHandlerProvider.createFileBodyHandler(null));
  }

  @Test
  void should_create_lines_handler() {
    val replacing = new Object();
    val actual = bodyHandlerProvider.createLinesBodyHandler();
    assertThat(actual)
      .usingRecursiveComparison()
      .isEqualTo(BodyHandlers.ofLines());
  }
}
