package com.github.badpop.celeritas.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

class CeleritasHttpClientNewClientTest {

  @Test
  void should_build_default_client() {
    val actual = CeleritasHttpClient.newClient();

    assertThat(actual).isNotNull();
    assertThat(actual.getObjectMapper()).isNotNull();
    assertThat(actual.getObjectMapper().getRegisteredModuleIds()).hasSize(2);
    assertThat(actual.getHttpClient()).isNotNull();
  }

  @Test
  void should_build_client_with_http_client() {
    val providedClient = HttpClient.newHttpClient();

    val actual = CeleritasHttpClient.newClient(providedClient);

    assertThat(actual).isNotNull();
    assertThat(actual.getObjectMapper()).isNotNull();
    assertThat(actual.getObjectMapper().getRegisteredModuleIds()).hasSize(2);
    assertThat(actual.getHttpClient()).isEqualTo(providedClient);
  }

  @Test
  void should_build_client_with_provided_mapper() {
    val providedOm = new ObjectMapper().registerModules(new VavrModule(), new JavaTimeModule());

    val actual = CeleritasHttpClient.newClient(providedOm);

    assertThat(actual).isNotNull();
    assertThat(actual.getObjectMapper()).isEqualTo(providedOm);
    assertThat(actual.getHttpClient()).isNotNull();
  }

  @Test
  void should_build_client() {
    val providedClient = HttpClient.newHttpClient();
    val providedOm = new ObjectMapper().registerModules(new VavrModule(), new JavaTimeModule());

    val actual = CeleritasHttpClient.newClient(providedClient, providedOm);

    assertThat(actual).isNotNull();
    assertThat(actual.getObjectMapper()).isEqualTo(providedOm);
    assertThat(actual.getHttpClient()).isEqualTo(providedClient);
  }

}
