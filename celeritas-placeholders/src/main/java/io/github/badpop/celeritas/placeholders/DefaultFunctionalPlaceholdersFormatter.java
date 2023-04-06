package io.github.badpop.celeritas.placeholders;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.github.badpop.celeritas.placeholders.DefaultPlaceholderConstants.DEFAULT_PLACEHOLDER_REGEX;
import static io.vavr.API.Try;

public class DefaultFunctionalPlaceholdersFormatter implements FunctionalPlaceholdersFormatter {

  private static final PlaceholdersFormatter DEFAULT_FORMATTER = PlaceholdersFormatter.newFormatter();

  @Override
  public Seq<String> getPlaceholdersAsSeq(String strFormat) {
    return Formatter.getPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat);
  }

  @Override
  public List<String> getPlaceholders(String strFormat) {
    return DEFAULT_FORMATTER.getPlaceholders(strFormat);
  }

  @Override
  public boolean hasPlaceholders(String strFormat) {
    return DEFAULT_FORMATTER.hasPlaceholders(strFormat);
  }

  @Override
  public int countPlaceholders(String strFormat) {
    return DEFAULT_FORMATTER.countPlaceholders(strFormat);
  }

  @Override
  public String format(String strTemplate, Map<String, Object> parameters) {
    return DEFAULT_FORMATTER.format(strTemplate, parameters);
  }

  @Override
  public String formatIgnoringUnknownPlaceholders(String strTemplate, Map<String, Object> parameters) {
    return DEFAULT_FORMATTER.formatIgnoringUnknownPlaceholders(strTemplate, parameters);
  }

  @Override
  public String formatOrElse(String strTemplate, Map<String, Object> parameters, Supplier<String> supplier) {
    return DEFAULT_FORMATTER.formatOrElse(strTemplate, parameters, supplier);
  }

  @Override
  public String formatIgnoringUnknownPlaceholdersOrElse(String strTemplate, Map<String, Object> parameters, Supplier<String> supplier) {
    return DEFAULT_FORMATTER.formatIgnoringUnknownPlaceholdersOrElse(strTemplate, parameters, supplier);
  }

  @Override
  public String formatOrElseThrow(String strTemplate, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable {
    return DEFAULT_FORMATTER.formatOrElseThrow(strTemplate, parameters, exceptionSupplier);
  }

  @Override
  public String formatIgnoringUnknownPlaceholdersOrElseThrow(String strTemplate, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable {
    return DEFAULT_FORMATTER.formatIgnoringUnknownPlaceholdersOrElseThrow(strTemplate, parameters, exceptionSupplier);
  }

  @Override
  //TODO TEST
  public String format(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Formatter.format(DEFAULT_PLACEHOLDER_REGEX, strFormat, parameters);
  }

  @Override
  //TODO TEST
  public Try<String> tryToFormat(String strFormat, Map<String, Object> parameters) {
    return Try(() -> format(strFormat, parameters));
  }

  @Override
  //TODO TEST
  public Try<String> tryToFormat(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Try(() -> format(strFormat, parameters));
  }

  @Override
  //TODO TEST
  public String formatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Formatter.formatIgnoringUnknownPlaceholders(DEFAULT_PLACEHOLDER_REGEX, strFormat, parameters);
  }

  @Override
  //TODO TEST
  public Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters) {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters));
  }

  @Override
  //TODO TEST
  public Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters));
  }
}
