package io.github.badpop.celeritas.placeholders;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface PlaceholdersFormatter {

  static PlaceholdersFormatter newFormatter() {
    return new DefaultPlaceholdersFormatter();
  }

  List<String> getPlaceholders(String strFormat);

  boolean hasPlaceholders(String strFormat);

  int countPlaceholders(String strFormat);

  String format(String strFormat, Map<String, Object> parameters);

  String formatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters);

  String formatOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier);

  String formatIgnoringUnknownPlaceholdersOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier);

  String formatOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable;

  String formatIgnoringUnknownPlaceholdersOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable;
}
