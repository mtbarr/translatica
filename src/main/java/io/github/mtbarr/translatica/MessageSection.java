package io.github.mtbarr.translatica;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * This class represents a section of the message registry.
 * Sections are separated by a dot.
 * <p>
 * Example:
 * foo.bar.baz
 * <p></>
 * Where foo is the section, bar is the subsection and baz is the key.
 *
 * @author Luiz Carlos Mourão (<a href="https://github.com/saiintbrisson">saiintbrisson</a>)
 */
public final class MessageSection {

  private final String sectionName;

  public MessageSection(@NotNull String sectionName) {
    this.sectionName = sectionName;
  }

  public String getSectionName() {
    return this.sectionName;
  }

  private String getKey(@NotNull String key) {
    if (key.isEmpty()) {
      throw new IllegalArgumentException("Key cannot be null or empty.");
    }

    return this.sectionName + "." + key;
  }

  /**
   * Gets a keyed message according to the provided Locale.
   *
   * @param locale message locale.
   * @param key    message key.
   * @return a translated message valued based on {@param locale} provided.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String getMessage(@NotNull Locale locale, @NotNull String key) {
    return MessageRegistry.getMessage(locale, getKey(key));
  }

  /**
   * Equivalent to {@link MessageRegistry#get(Locale, String)}
   * but with replacements being provided.
   *
   * @param locale       message locale
   * @param key          message key
   * @param replacements message replacements
   * @return a final replaced version of initial keyed message.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String getMessage(@NotNull Locale locale, @NotNull String key, @NotNull Object... replacements) {
    return MessageRegistry.getMessage(locale, getKey(key), replacements);
  }

  /**
   * Gets a keyed message according to the default Locale.
   *
   * @param key message key.
   * @return a translated message valued based on default locale.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String getMessage(@NotNull String key) {
    return MessageRegistry.getMessage(getKey(key));
  }

  /**
   * Equivalent to {@link MessageRegistry#get(String)}
   * but with replacements being provided.
   *
   * @param key          message key
   * @param replacements message replacements
   * @return a final replaced version of initial keyed message.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String getMessage(@NotNull String key, @NotNull Object... replacements) {
    return MessageRegistry.getMessage(getKey(key), replacements);
  }

  /**
   * Equivalent to {@link MessageRegistry#get(String, Object...)}
   * but using {@link MessageFormat#format(String, Object...)}
   *
   * @param key          message key
   * @param replacements message replacements
   * @return a final replaced version of initial keyed message.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String getFormattedMessage(@NotNull String key, Object... replacements) {
    return MessageRegistry.getFormattedMessage(getKey(key), replacements);
  }

  /**
   * Get the sub-section of this section.
   *
   * @param subSection the name of the sub-section.
   * @return the sub-section.
   */
  @NotNull
  public MessageSection getSubSection(@NotNull String subSection) {
    return new MessageSection(getKey(subSection));
  }
}