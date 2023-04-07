package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.NoPlaceholderFoundException;
import io.github.badpop.celeritas.placeholders.exception.NullParametersException;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.vavr.API.Try;

/**
 * The DefaultFunctionalPlaceholderFormatter is a default formatter that provides a more complete API
 * for working with functional objects offered by the Vavr library.
 *
 * <p> The default placeholder format is ${[A-Za-z0-9_-]+}
 */
public class DefaultFunctionalPlaceholderFormatter implements FunctionalPlaceholderFormatter {

  private static final PlaceholderFormatter DEFAULT_FORMATTER = PlaceholderFormatter.newFormatter();
  private static final PlaceholderConfiguration PLACEHOLDER_CONFIGURATION = PlaceholderConfiguration.newDefault();

  @Override
  public Seq<String> getPlaceholdersAsSeq(String strFormat) {
    return Formatter.getPlaceholders(PLACEHOLDER_CONFIGURATION, strFormat);
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
  public String format(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    if(!hasPlaceholders(strFormat)) {
      throw new NoPlaceholderFoundException();
    }

    if(parameters == null) {
      throw new NullParametersException();
    }

    return Formatter.format(PLACEHOLDER_CONFIGURATION, strFormat, parameters);
  }

  @Override
  public Try<String> tryToFormat(String strFormat, Map<String, Object> parameters) {
    return Try(() -> format(strFormat, parameters));
  }

  @Override
  public Try<String> tryToFormat(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Try(() -> format(strFormat, parameters));
  }

  @Override
  public String formatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    if(parameters == null) {
      throw new NullParametersException();
    }

    return Formatter.formatIgnoringUnknownPlaceholders(PLACEHOLDER_CONFIGURATION, strFormat, parameters);
  }

  @Override
  public Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters) {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters));
  }

  @Override
  public Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters) {
    return Try(() -> formatIgnoringUnknownPlaceholders(strFormat, parameters));
  }
}
