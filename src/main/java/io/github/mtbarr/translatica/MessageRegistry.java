package io.github.mtbarr.translatica;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A registry of keyed messages with the source {@link Locale}.
 *
 * @author Matheus Barreto
 */
public final class MessageRegistry {


  private static MessageRegistry instance;

  private final Map<Locale, List<ResourceBundle>> bundles = new ConcurrentHashMap<>();

  public MessageRegistry(@NotNull String propertiesFileName) {
    this.registerMessages(PropertyResourceBundle.getBundle(propertiesFileName));
  }

  /**
   * Default constructor using 'messages' file
   */
  public MessageRegistry() {
    this("messages");
  }


  /**
   * Register all messages present in the provided resource bundle.
   *
   * @param resourceBundle ResourceBundle containing all messages.
   **/
  public void registerMessages(@NotNull ResourceBundle resourceBundle) {
    List<ResourceBundle> bundleList = bundles.computeIfAbsent(resourceBundle.getLocale(), key -> new CopyOnWriteArrayList<>());
    bundleList.add(resourceBundle);
  }

  /**
   * Register all messages from the provided ResourceBundle that are registered.
   *
   * @param resourceBundle provided resouce bundle.
   */
  public void unregisterMessages(@NotNull ResourceBundle resourceBundle) {
    List<ResourceBundle> bundleList = bundles.get(resourceBundle.getLocale());
    if (bundleList != null) {
      bundleList.removeIf(bundle -> bundle.getLocale().equals(resourceBundle.getLocale()));
    }
  }

  /**
   * Gets a keyed message according to the provided Locale.
   *
   * @param locale message locale.
   * @param key    message key.
   * @return a translated message valued based on {@param locale} provided.
   * @throws IllegalArgumentException if the message value is not found.
   */
  public String get(Locale locale, String key) {
    List<ResourceBundle> bundleList = bundles.get(locale);
    if (bundleList == null) {
      throw new IllegalArgumentException("Cannot found resource bundle for this locale.");
    }

    for (ResourceBundle bundle : bundleList) {
      return this.findBundleMessage(key, bundle);
    }

    throw new NullPointerException("Cannot found message keyed with " + key + " key on locale " + locale.getDisplayName());
  }


  /**
   * Gets a keyed message according to the default Locale.
   *
   * @param key message key.
   * @return a translated message valued based on default locale.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public String get(String key) {
    return get(Locale.getDefault(), key);
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
  public String get(Locale locale, String key, Object... replacements) {
    return String.format(get(locale, key), replacements);
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
  public String get(String key, Object... replacements) {
    return get(Locale.getDefault(), key, replacements);
  }

  /**
   * Gets the main instance of the message registry.
   */
  public static MessageRegistry getInstance() {
    if (instance == null) {
      instance = new MessageRegistry();
    }

    return instance;
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
  public static String getMessage(@NotNull Locale locale, @NotNull String key) {
    return getInstance().get(locale, key);
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
  public static String getMessage(@NotNull Locale locale, @NotNull String key, @NotNull Object... replacements) {
    return getInstance().get(locale, key, replacements);
  }

  /**
   * Gets a keyed message according to the default Locale.
   *
   * @param key message key.
   * @return a translated message valued based on default locale.
   * @throws IllegalArgumentException if the message value is not found.
   */
  @NotNull
  public static String getMessage(@NotNull String key) {
    return getInstance().get(key);
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
  public static String getMessage(@NotNull String key, @NotNull Object... replacements) {
    return getInstance().get(key, replacements);
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
  public static String getFormattedMessage(@NotNull String key, Object... replacements) {
    return MessageFormat.format(getMessage(key), replacements);
  }

  /**
   * Get a section of the message registry.
   * <p>
   * Example:
   * foo.bar.baz
   * <p></>
   * Where foo is the section, bar is the subsection and baz is the key.
   *
   * @param sectionName the section name.
   * @return a section of the message registry.
   */
  public static MessageSection getSection(String sectionName) {
    return new MessageSection(sectionName);
  }

  private static String encodeToUtf8(String value) {
    byte[] bytes = value.getBytes();
    return new String(bytes, StandardCharsets.UTF_8);
  }

  private String findBundleMessage(String key, ResourceBundle bundle) {
    try {
      return bundle.getString(key);
    } catch (Exception e) {
      throw new NullPointerException("Cannot found message keyed with " + key + " key on bundle " + bundle.getBaseBundleName());
    }
  }
}