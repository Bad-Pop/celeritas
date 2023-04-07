package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.NoPlaceholderFoundException;
import io.github.badpop.celeritas.placeholders.exception.NullParametersException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;
import io.vavr.collection.Seq;
import io.vavr.control.Try;

import java.util.List;
import java.util.Map;

/**
 * A FunctionalPlaceholderFormatter is a formatter allowing to work with functional types proposed by the Vavr library.
 * The default implementation is {@link DefaultFunctionalPlaceholderFormatter}
 */
public interface FunctionalPlaceholderFormatter extends PlaceholderFormatter {

  /**
   * Returns a new instance of {@link DefaultFunctionalPlaceholderFormatter}
   *
   * @return a new DefaultFunctionalPlaceholderFormatter
   */
  static FunctionalPlaceholderFormatter newFunctionalFormatter() {
    return new DefaultFunctionalPlaceholderFormatter();
  }

  /**
   * This method, like the {@link #getPlaceholders(String)} method, allows you to find the placeholders present in a string,
   * with the difference that here you get a {@link Seq} and not a {@link List}
   *
   * @param strFormat a string with or without placeholders
   * @return the placeholders present in the given string
   */
  Seq<String> getPlaceholdersAsSeq(String strFormat);

  /**
   * Formats a string that contains placeholders
   *
   * <p> The keys of the map passed in parameter must be equal to the keys of the placeholders present in the provided string.
   * Be careful, the keys are case sensitive. For example, if you have a placeholder whose key is "myPlaceholderKey",
   * then the map must contain an entry whose key is equal to "myPlaceholderKey". The order of the keys in the map does not matter.
   *
   * <p> This method will fail if a placeholder in the string is not provided in the map or if the string has no placeholder.
   * If you want to format a string with unknown placeholders or no placeholders at all you should use the {@link #formatIgnoringUnknownPlaceholders(String, Map)}
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a formatted string with placeholders replaced by there values
   * @throws NoPlaceholderFoundException if the given string does not contain any placeholder
   * @throws NullParametersException     if the given map is null
   * @throws PlaceholderFormatException  if an error occurred while trying to format the given string
   */
  String format(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  /**
   * This method calls the {@link #format(String, Map)} method directly but wraps the execution of the latter in a functional {@link Try} and returns it as is.
   * Thus, no exceptions can be thrown when you call this method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a Try containing the execution result
   */
  Try<String> tryToFormat(String strFormat, Map<String, Object> parameters);

  /**
   * This method calls the {@link #format(String, io.vavr.collection.Map)} method directly but wraps the execution of the latter in a functional {@link Try} and returns it as is.
   * Thus, no exceptions can be thrown when you call this method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a Try containing the execution result
   */
  Try<String> tryToFormat(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  /**
   * Formats a string that contains placeholders
   *
   * <p> The keys of the map passed in parameter must be equal to the keys of the placeholders present in the provided string.
   * Be careful, the keys are case sensitive. For example, if you have a placeholder whose key is "myPlaceholderKey",
   * then the map must contain an entry whose key is equal to "myPlaceholderKey". The order of the keys in the map does not matter.
   *
   * <p> This method will ignore unknown placeholders and won't fail if the string has no placeholder
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a formatted string with placeholders replaced by there values
   * @throws NullParametersException if the given map is null
   * @throws PlaceholderFormatException if an error occurred while trying to format the given string
   */
  String formatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters);

  /**
   * This method calls the {@link #formatIgnoringUnknownPlaceholders(String, Map)} method directly but wraps the execution of the latter in a functional {@link Try} and returns it as is.
   * Thus, no exceptions can be thrown when you call this method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a Try containing the execution result
   */
  Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters);

  /**
   * This method calls the {@link #formatIgnoringUnknownPlaceholders(String, io.vavr.collection.Map)} method directly but wraps the execution of the latter in a functional {@link Try} and returns it as is.
   * Thus, no exceptions can be thrown when you call this method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @return a Try containing the execution result
   */
  Try<String> tryToFormatIgnoringUnknownPlaceholders(String strFormat, io.vavr.collection.Map<String, Object> parameters);
}
