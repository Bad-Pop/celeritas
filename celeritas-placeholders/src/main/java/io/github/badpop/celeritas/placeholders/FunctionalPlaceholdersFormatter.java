package io.github.badpop.celeritas.placeholders;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.util.Map;

public interface FunctionalPlaceholdersFormatter extends PlaceholdersFormatter {

  static FunctionalPlaceholdersFormatter newFunctionalFormatter() {
    return new DefaultFunctionalPlaceholdersFormatter();
  }

  Seq<String> getPlaceholdersAsSeq(String strFormat);

  String format(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  Try<String> tryToFormat(String strFormat, Map<String, Object> parameters);

  Try<String> tryToFormat(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  String formatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters);

  Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters);
}
