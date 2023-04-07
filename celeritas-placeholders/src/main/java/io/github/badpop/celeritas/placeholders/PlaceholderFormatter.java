package io.github.badpop.celeritas.placeholders;

import io.github.badpop.celeritas.placeholders.exception.NoPlaceholderFoundException;
import io.github.badpop.celeritas.placeholders.exception.NullParametersException;
import io.github.badpop.celeritas.placeholders.exception.PlaceholderFormatException;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides a mechanism to replace placeholders in a string with values given in parameters.
 * <p> The default PlaceholderFormatter implementation is {@link DefaultPlaceholderFormatter}.
 */
public interface PlaceholderFormatter {

  /**
   * Create a new instance of {@link DefaultPlaceholderFormatter}.
   * <p>The default placeholder format is "${[A-Za-z0-9_-]+}".
   *
   * @return a new {@link DefaultPlaceholderFormatter} instance
   */
  static PlaceholderFormatter newFormatter() {
    return new DefaultPlaceholderFormatter();
  }

  /**
   * Get a list containing the key of all placeholders present in a string
   *
   * @param strFormat a string containing or not placeholders
   * @return a list of strings representing the keys of the placeholders found or an empty list if the given string does not contain placeholders
   */
  List<String> getPlaceholders(String strFormat);

  /**
   * Check if a string contains placeholders
   *
   * @param strFormat a string containing or not placeholders
   * @return true if the string contains one or more placeholder, false otherwise
   */
  boolean hasPlaceholders(String strFormat);

  /**
   * Get the amount of placeholders in a string
   *
   * @param strFormat a string containing or not placeholders
   * @return the number of placeholders in the given string
   * @throws IllegalArgumentException if the underlying placeholder pattern (modelled by a regular expression) is invalid
   */
  int countPlaceholders(String strFormat);

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
   * @throws NullParametersException if the given map is null
   * @throws PlaceholderFormatException if an error occurred while trying to format the given string
   */
  String format(String strFormat, Map<String, Object> parameters);

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
  String formatIgnoringUnknownPlaceholders(String strFormat, Map<String, Object> parameters);

  /**
   * This method allow you to {@link #format(String, Map)} a string but return a default string if the format fail.
   * For more details on failure reasons take a look at the {@link #format(String, Map)} method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @param supplier   the string to return if the format failed
   * @return a formatted string if no error occurred, otherwise the default string given as supplier
   */
  String formatOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier);

  /**
   * This method allow you to {@link #formatIgnoringUnknownPlaceholders(String, Map)} a string but return a default string if the format fail.
   * For more details on failure reasons take a look at the {@link #formatIgnoringUnknownPlaceholders(String, Map)} method.
   *
   * @param strFormat  a string containing or not placeholders
   * @param parameters a Map containing values to replace the string placeholders
   * @param supplier   the string to return if the format failed
   * @return a formatted string if no error occurred, otherwise the default string given as supplier
   */
  String formatIgnoringUnknownPlaceholdersOrElse(String strFormat, Map<String, Object> parameters, Supplier<String> supplier);

  /**
   * This method allow you to {@link #format(String, Map)} a string or throws a custom exception if the format fail.
   * For more details on failure reasons check the {@link #format(String, Map)} method.
   *
   * @param strFormat         a string containing or not placeholders
   * @param parameters        a Map containing values to replace the string placeholders
   * @param exceptionSupplier the exception to throws if the format fail
   * @return a formatted string if no error occurred, otherwise throws the exception given into the supplier
   */
  String formatOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable;

  /**
   * This method allow you to {@link #formatIgnoringUnknownPlaceholders(String, Map)} a string or throws a custom exception if the format fail.
   * For more details on failure reasons check the {@link #formatIgnoringUnknownPlaceholders(String, Map)} method.
   *
   * @param strFormat         a string containing or not placeholders
   * @param parameters        a Map containing values to replace the string placeholders
   * @param exceptionSupplier the exception to throws if the format fail
   * @return a formatted string if no error occurred, otherwise throws the exception given into the supplier
   */
  String formatIgnoringUnknownPlaceholdersOrElseThrow(String strFormat, Map<String, Object> parameters, Supplier<? extends Throwable> exceptionSupplier) throws Throwable;
}
