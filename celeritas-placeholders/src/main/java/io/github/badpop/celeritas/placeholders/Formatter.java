package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderParameterNotFound;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static io.vavr.API.Seq;

/**
 * This interface is the core of the celeritas-placeholder library.
 * It outlines the basic methods for all formatters to work without having to rewrite the placeholder formatting process.
 */
interface Formatter {

  static Seq<String> getPlaceholders(PlaceholderConfiguration placeholderConfiguration, String strFormat) {
    final var keys = new ArrayList<String>();

    try {
      final var matcher = Pattern.compile(placeholderConfiguration.getPattern()).matcher(strFormat);
      while (matcher.find()) {
        keys.add(matcher.group(1));
      }
    } catch (Exception e) {
      return Seq();
    }

    return List.ofAll(keys);
  }

  static boolean hasPlaceholders(PlaceholderConfiguration placeholderConfiguration, String strFormat) {
    try {
      return Pattern.compile(placeholderConfiguration.getPattern()).matcher(strFormat).find();
    } catch (Exception e) {
      return false;
    }
  }

  static int countPlaceholders(PlaceholderConfiguration placeholderConfiguration, String strFormat) {
    try {
      final var matcher = Pattern.compile(placeholderConfiguration.getPattern()).matcher(strFormat);
      var count = 0;
      while (matcher.find()) {
        count++;
      }
      return count;
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to match format [" + strFormat + "] with pattern [" + placeholderConfiguration.getPattern() + "]", e);
    }
  }

  static String format(PlaceholderConfiguration placeholderConfiguration, String strFormat, Map<String, Object> parameters) {
    try {
      final var newTemplate = new StringBuilder(strFormat);
      final var paramsValues = new ArrayList<>();
      final var matcher = Pattern.compile(placeholderConfiguration.getPattern()).matcher(strFormat);

      while (matcher.find()) {
        String key = matcher.group(1);
        String placeholder = placeholderConfiguration.getPrefix() + key + placeholderConfiguration.getSuffix();
        int index = newTemplate.indexOf(placeholder);

        if (index != -1) {
          newTemplate.replace(index, index + placeholder.length(), "%s");

          parameters.find(tuple -> tuple._1.equals(key))
            .map(Tuple2::_2)
            .peek(paramsValues::add)
            .getOrElseThrow(() -> new PlaceholderParameterNotFound("No parameter found for the placeholder " + placeholder));

        }
      }

      return String.format(newTemplate.toString(), paramsValues.toArray());
    } catch (Exception e) {
      throw new PlaceholderFormatException("Unable to format the requested String", e);
    }
  }

  static String formatIgnoringUnknownPlaceholders(PlaceholderConfiguration placeholderConfiguration, String strFormat, Map<String, Object> parameters) {
    try {
      final var newTemplate = new StringBuilder(strFormat);
      final var paramsValues = new ArrayList<>();
      final var matcher = Pattern.compile(placeholderConfiguration.getPattern()).matcher(strFormat);

      while (matcher.find()) {
        String key = matcher.group(1);
        String placeholder = placeholderConfiguration.getPrefix() + key + placeholderConfiguration.getSuffix();
        int index = newTemplate.indexOf(placeholder);

        if (index != -1) {
          newTemplate.replace(index, index + placeholder.length(), "%s");

          parameters.find(tuple -> tuple._1.equals(key))
            .map(Tuple2::_2)
            .peek(paramsValues::add)
            .onEmpty(() -> paramsValues.add(placeholder));
        }
      }

      return String.format(newTemplate.toString(), paramsValues.toArray());
    } catch (Exception e) {
      throw new PlaceholderFormatException("Unable to format the requested String", e);
    }
  }
}
