package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.NoPlaceholderFoundException;
import io.github.badpop.celeritas.placeholders.exception.NullParametersException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;
import io.vavr.collection.HashMap;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.github.badpop.celeritas.placeholders.DefaultPlaceholderConstants.DEFAULT_PLACEHOLDER_REGEX;
import static io.vavr.API.Try;

public class DefaultPlaceholdersFormatter implements PlaceholdersFormatter {

  @Override
  public List<String> getPlaceholders(String strFormat) {
    return Formatter.getPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat).toJavaList();
  }

  @Override
  public boolean hasPlaceholders(String strFormat) {
    return Formatter.hasPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat);
  }

  @Override
  public int countPlaceholders(String strFormat) {
    return Formatter.countPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat);
  }

  @Override
  public String format(String strFormat, Map<String, Object> parameters) {
    if (!hasPlaceholders(strFormat)) {
      throw new NoPlaceholderFoundException();
    }

    if(parameters == null) {
      throw new NullParametersException();
    }

    return Formatter.format(DEFAULT_PLACEHOLDER_REGEX, strFormat, HashMap.ofAll(parameters));
  }

  @Override
  public String formatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters) {
    if(parameters == null) {
      throw new PlaceholderFormatException("Unable to format string with null parameters");
    }

    return Formatter.formatIgnoringUnknownPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat, HashMap.ofAll(parameters));
  }

  @Override
  public String formatOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier) {
    return Try(() -> format(strFormat, parameters)).getOrElse(supplier);
  }

  @Override
  public String formatIgnoringUnknownPlaceholdersOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier) {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters)).getOrElse(supplier);
  }

  @Override
  public String formatOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable {
    return Try(() -> format(strFormat, parameters)).getOrElseThrow(exceptionSupplier);
  }

  @Override
  public String formatIgnoringUnknownPlaceholdersOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters)).getOrElseThrow(exceptionSupplier);
  }
}
