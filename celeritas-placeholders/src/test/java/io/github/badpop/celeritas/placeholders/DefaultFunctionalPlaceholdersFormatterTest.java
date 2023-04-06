package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.NoPlaceholderFoundException;
import io.github.badpop.celeritas.placeholders.exception.NullParametersException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderParameterNotFound;
import io.vavr.API;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DefaultFunctionalPlaceholdersFormatterTest {

  private final FunctionalPlaceholdersFormatter formatter = FunctionalPlaceholdersFormatter.newFunctionalFormatter();

  @ParameterizedTest
  @MethodSource("should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments")
  void should_return_placeholders_keys_or_empty_list_if_no_placeholder_as_seq(String strFormat, Seq<String> expected) {
    assertThat(formatter.getPlaceholdersAsSeq(strFormat)).containsExactlyElementsOf(expected);
  }

  @Nested
  class DefaultFormatterTest {
    @ParameterizedTest
    @MethodSource("should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments")
    void should_return_placeholders_keys_or_empty_list_if_no_placeholder(String strFormat, Seq<String> expected) {
      assertThat(formatter.getPlaceholders(strFormat)).containsExactlyElementsOf(expected);
    }

    @ParameterizedTest
    @MethodSource("should_determine_if_there_is_placeholders_or_not_arguments")
    void should_determine_if_there_is_placeholders_or_not(String strFormat, boolean expected) {
      assertThat(formatter.hasPlaceholders(strFormat)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("should_count_placeholders_arguments")
    void should_count_placeholders(String strFormat, int expected) {
      assertThat(formatter.countPlaceholders(strFormat)).isEqualTo(expected);
    }

    @Nested
    class FormatTest {

      @Test
      void should_format() {
        final var format = "An incredibly complex ${String} with many ${Many-PlaceH0lderS}";
        final var parameters = new HashMap<String, Object>();
        parameters.put("String", "replaced");
        parameters.put("Many-PlaceH0lderS", 22);

        assertThat(formatter.format(format, parameters)).isEqualTo("An incredibly complex replaced with many 22");
      }

      @Test
      void should_not_format_on_null_format() {
        assertThatExceptionOfType(NoPlaceholderFoundException.class)
          .isThrownBy(() -> formatter.format(null, new HashMap<>()))
          .withMessage("No placeholder was found into the given string format");
      }

      @Test
      void should_not_format_on_null_parameters() {
        assertThatExceptionOfType(NullParametersException.class)
          .isThrownBy(() -> formatter.format("${a_placeholder}", (java.util.Map) null))
          .withMessage("Unable to format a string if parameters null");
      }

      @Test
      void should_not_format_on_unknown_placeholder() {
        final var format = "A string with an ${unknownPlaceholder}";
        assertThatExceptionOfType(PlaceholderFormatException.class)
          .isThrownBy(() -> formatter.format(format, new HashMap<>()))
          .withMessage("Unable to format the requested String")
          .withCauseInstanceOf(PlaceholderParameterNotFound.class);
      }
    }

    @Nested
    class FormatIgnoringUnknownPlaceholdersTest {

      @ParameterizedTest
      @MethodSource("should_format_arguments")
      void should_format_even_if_a_placeholder_is_unknown(String strFormat, Map<String, Object> parameters, String expected) {
        assertThat(formatter.formatIgnoringUnknownPlaceholders(strFormat, parameters.toJavaMap())).isEqualTo(expected);
      }

      @Test
      void should_not_format_on_null_format() {
        assertThatExceptionOfType(PlaceholderFormatException.class)
          .isThrownBy(() -> formatter.formatIgnoringUnknownPlaceholders(null, new HashMap<>()))
          .withMessage("Unable to format the requested String")
          .withCauseInstanceOf(NullPointerException.class);
      }

      @Test
      void should_not_format_on_null_parameters() {
        assertThatExceptionOfType(PlaceholderFormatException.class)
          .isThrownBy(() -> formatter.formatIgnoringUnknownPlaceholders("${a_placeholder}", (java.util.Map) null))
          .withMessage("Unable to format string with null parameters");
      }

      private static Stream<Arguments> should_format_arguments() {
        return Stream.of(
          Arguments.of(
            "An exemple String with two ${place}, [${holder_more_complicated06}]",
            API.Map("place", "PLACE", "holder_more_complicated06", 22),
            "An exemple String with two PLACE, [22]"),
          Arguments.of(
            "An exemple String without placeholders",
            API.Map("place", "PLACE", "holder_more_complicated06", 22),
            "An exemple String without placeholders"),
          Arguments.of(
            "An exemple String without placeholders",
            API.Map(),
            "An exemple String without placeholders"),
          Arguments.of(
            "An exemple String with an ${unknownPlaceholders}",
            API.Map(),
            "An exemple String with an ${unknownPlaceholders}"));
      }
    }

    @Test
    void should_format_or_else() {
      final var orElse = "something";
      final var actual = formatter.formatOrElse(null, null, () -> orElse);
      assertThat(actual).isEqualTo(orElse);
    }

    @Test
    void should_format_ignoring_unknown_placeholders_or_else() {
      final var orElse = "something";
      final var actual = formatter.formatIgnoringUnknownPlaceholdersOrElse(null, null, () -> orElse);
      assertThat(actual).isEqualTo(orElse);
    }

    @Test
    void should_format_or_else_throw() {
      final var throwable = new UnsupportedOperationException("error");
      assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> formatter.formatOrElseThrow(null, null, () -> throwable))
        .withMessage(throwable.getMessage());
    }

    @Test
    void should_format_ignoring_unknown_placeholders_or_else_throw() {
      final var throwable = new UnsupportedOperationException("error");
      assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> formatter.formatIgnoringUnknownPlaceholdersOrElseThrow(null, null, () -> throwable))
        .withMessage(throwable.getMessage());
    }

    private static Stream<Arguments> should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments() {
      return Stream.of(
        Arguments.of(
          "An exemple String with two ${place}, [${holder_more_complicated06}]",
          API.Seq("place", "holder_more_complicated06")),
        Arguments.of(
          "An exemple string without placeholder",
          API.Seq()));
    }

    private static Stream<Arguments> should_determine_if_there_is_placeholders_or_not_arguments() {
      return Stream.of(
        Arguments.of("An exemple String with two ${place}, [${holder_more_complicated06}]", true),
        Arguments.of("An exemple string without placeholder", false));
    }

    private static Stream<Arguments> should_count_placeholders_arguments() {
      return Stream.of(
        Arguments.of("An exemple String with two ${place}, [${holder_more_complicated06}]", 2),
        Arguments.of("An exemple string without placeholder", 0));
    }
  }

  private static Stream<Arguments> should_return_placeholders_keys_or_empty_list_if_no_placeholder_arguments() {
    return Stream.of(
      Arguments.of(
        "An exemple String with two ${place}, [${holder_more_complicated06}]",
        API.Seq("place", "holder_more_complicated06")),
      Arguments.of(
        "An exemple string without placeholder",
        API.Seq()));
  }
}
