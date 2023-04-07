package io.github.badpop.celeritas.placeholders;

import static io.github.badpop.celeritas.placeholders.DefaultPlaceholderConstants.*;

/**
 * A PlaceholderConfiguration is a representation of the format of a placeholder.
 * The pattern must be a regular expression with a single capture group to identify the placeholder key.
 * The prefix and suffix allow the placeholder to be recreated at formatting time from the key to format it.
 *
 * <p> The default implementation of this class is {@link DefaultPlaceholderConfiguration}
 */
public class PlaceholderConfiguration {

  private final String pattern;
  private final String prefix;
  private final String suffix;

  public PlaceholderConfiguration(String pattern, String prefix, String suffix) {
    this.pattern = pattern;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public static PlaceholderConfiguration newDefault() {
    return new DefaultPlaceholderConfiguration();
  }

  public String getPattern() {
    return pattern;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  public static class DefaultPlaceholderConfiguration extends PlaceholderConfiguration {
    public DefaultPlaceholderConfiguration() {
      super(DEFAULT_PLACEHOLDER_PATTERN, DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX);
    }
  }
}
