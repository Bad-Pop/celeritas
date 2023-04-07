package io.github.badpop.celeritas.placeholders;

import java.util.Map;

interface DefaultPlaceholderConstants {

  String DEFAULT_PLACEHOLDER_PATTERN = "[$][{]([\\w\\d_-]+)}";
  String DEFAULT_PLACEHOLDER_PREFIX = "${";
  String DEFAULT_PLACEHOLDER_SUFFIX = "}";


  Map<String, Object> toto = Map.of();
}
