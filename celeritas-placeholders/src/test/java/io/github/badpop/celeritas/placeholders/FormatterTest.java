package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderParameterNotFound;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import static io.vavr.API.Map;
import static io.vavr.API.Seq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FormatterTest {

  private static final PlaceholderConfiguration PLACEHOLDER_CONFIGURATION = PlaceholderConfiguration.newDefault();

  @ParameterizedTest
  @MethodSource("should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments")
  void should_return_placeholders_keys_or_empty_list_if_no_placeholder(PlaceholderConfiguration placeholderConfiguration, String strFormat, Seq<String> expected) {
    assertThat(Formatter.getPlaceholders(placeholderConfiguration, strFormat)).containsExactlyElementsOf(expected);
  }

  @Test
  void should_return_empty_seq_on_invalid_regex() {
    final var invalidRegex = "[$]{([\\\\w\\d_-]+)}";
    final var placeholderConfiguration = new PlaceholderConfiguration(invalidRegex, null, null);
    final var format = "a string";
    VavrAssertions.assertThat(Formatter.getPlaceholders(placeholderConfiguration, format)).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("should_determine_if_there_is_placeholders_or_not_arguments")
  void should_determine_if_there_is_placeholders_or_not(PlaceholderConfiguration placeholderConfiguration, String strFormat, boolean expected) {
    assertThat(Formatter.hasPlaceholders(placeholderConfiguration, strFormat)).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("should_count_placeholders_arguments")
  void should_count_placeholders(PlaceholderConfiguration placeholderConfiguration, String strFormat, int expected) {
    assertThat(Formatter.countPlaceholders(placeholderConfiguration, strFormat)).isEqualTo(expected);
  }

  @Test
  void should_not_count_placeholders_on_invalid_regex() {
    final var invalidRegex = "[$]{([\\\\w\\d_-]+)}";
    final var placeholderConfiguration = new PlaceholderConfiguration(invalidRegex, null, null);
    final var format = "a string";
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> Formatter.countPlaceholders(placeholderConfiguration, format))
      .withMessage("Unable to match format [" + format + "] with pattern [" + invalidRegex + "]")
      .withCauseInstanceOf(PatternSyntaxException.class);
  }

  @Nested
  class FormatTest {

    @ParameterizedTest
    @MethodSource("should_format_arguments")
    void should_format(PlaceholderConfiguration placeholderConfiguration, String strFormat, Map<String, Object> parameters, String expected) {
      assertThat(Formatter.format(placeholderConfiguration, strFormat, parameters)).isEqualTo(expected);
    }

    @Test
    void should_not_format_on_invalid_regex() {
      final var invalidRegex = "[$]{([\\\\w\\d_-]+)}";
      final var placeholderConfiguration = new PlaceholderConfiguration(invalidRegex, null, null);
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.format(placeholderConfiguration, "", Map()))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(PatternSyntaxException.class);
    }

    @Test
    void should_not_format_on_null_format() {
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.format(PLACEHOLDER_CONFIGURATION, null, Map()))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void should_not_format_on_null_parameters() {
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.format(PLACEHOLDER_CONFIGURATION, "${a_placeholder}", null))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void should_not_format_on_unknown_placeholder() {
      final var format = "A string with an ${unknownPlaceholder}";
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.format(PLACEHOLDER_CONFIGURATION, format, Map()))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(PlaceholderParameterNotFound.class);
    }

    private static Stream<Arguments> should_format_arguments() {
      return Stream.of(
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String with two ${place}, [${holder_more_complicated06}]",
          Map("place", "PLACE", "holder_more_complicated06", 22),
          "An exemple String with two PLACE, [22]"),
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String without placeholders",
          Map("place", "PLACE", "holder_more_complicated06", 22),
          "An exemple String without placeholders"),
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String without placeholders",
          Map(),
          "An exemple String without placeholders"));
    }
  }

  @Nested
  class FormatIgnoringUnknownPlaceholdersTest {

    @ParameterizedTest
    @MethodSource("should_format_arguments")
    void should_format_even_if_a_placeholder_is_unknown(PlaceholderConfiguration placeholderConfiguration, String strFormat, Map<String, Object> parameters, String expected) {
      assertThat(Formatter.formatIgnoringUnknownPlaceholders(placeholderConfiguration, strFormat, parameters)).isEqualTo(expected);
    }

    @Test
    void should_not_format_on_invalid_regex() {
      final var invalidRegex = "[$]{([\\\\w\\d_-]+)}";
      final var placeholderConfiguration = new PlaceholderConfiguration(invalidRegex, null, null);
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.format(placeholderConfiguration, "", Map()))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(PatternSyntaxException.class);
    }

    @Test
    void should_not_format_on_null_format() {
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.formatIgnoringUnknownPlaceholders(PLACEHOLDER_CONFIGURATION, null, Map()))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void should_not_format_on_null_parameters() {
      assertThatExceptionOfType(PlaceholderFormatException.class)
        .isThrownBy(() -> Formatter.formatIgnoringUnknownPlaceholders(PLACEHOLDER_CONFIGURATION, "${a_placeholder}", null))
        .withMessage("Unable to format the requested String")
        .withCauseInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> should_format_arguments() {
      return Stream.of(
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String with two ${place}, [${holder_more_complicated06}]",
          Map("place", "PLACE", "holder_more_complicated06", 22),
          "An exemple String with two PLACE, [22]"),
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String without placeholders",
          Map("place", "PLACE", "holder_more_complicated06", 22),
          "An exemple String without placeholders"),
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String without placeholders",
          Map(),
          "An exemple String without placeholders"),
        Arguments.of(
          PLACEHOLDER_CONFIGURATION,
          "An exemple String with an ${unknownPlaceholders}",
          Map(),
          "An exemple String with an ${unknownPlaceholders}"));
    }
  }

  private static Stream<Arguments> should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments() {
    return Stream.of(
      Arguments.of(
        PLACEHOLDER_CONFIGURATION,
        "An exemple String with two ${place}, [${holder_more_complicated06}]",
        Seq("place", "holder_more_complicated06")),
      Arguments.of(
        PLACEHOLDER_CONFIGURATION,
        "An exemple string without placeholder",
        Seq()));
  }

  private static Stream<Arguments> should_determine_if_there_is_placeholders_or_not_arguments() {
    return Stream.of(
      Arguments.of(PLACEHOLDER_CONFIGURATION, "An exemple String with two ${place}, [${holder_more_complicated06}]", true),
      Arguments.of(PLACEHOLDER_CONFIGURATION, "An exemple string without placeholder", false));
  }

  private static Stream<Arguments> should_count_placeholders_arguments() {
    return Stream.of(
      Arguments.of(PLACEHOLDER_CONFIGURATION, "An exemple String with two ${place}, [${holder_more_complicated06}]", 2),
      Arguments.of(PLACEHOLDER_CONFIGURATION, "An exemple string without placeholder", 0));
  }
}
