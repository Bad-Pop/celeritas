package io.github.badpop.celeritas.http.client.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@lombok.Value
@AllArgsConstructor
public class Value {
  @JsonProperty(value = "value")
  int value;
}
