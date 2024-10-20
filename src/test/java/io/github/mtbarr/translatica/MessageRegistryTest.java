package io.github.mtbarr.translatica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class MessageRegistryTest {

  private MessageRegistry messageRegistry;

  @BeforeEach
  void setUp() {
    messageRegistry = MessageRegistry.getInstance();
  }

  @Test
  void getMessageWithDefaultLocale() {
    String message = messageRegistry.get("welcome.message");
    assertNotNull(message);
    assertEquals("Bem-vindo", message);
  }

  @Test
  void getMessageWithSpecificLocale() {
    Locale locale = new Locale("en", "US");
    ResourceBundle bundle = PropertyResourceBundle.getBundle("messages", locale);
    messageRegistry.registerMessages(bundle);
    String message = messageRegistry.get(locale, "welcome.message");
    assertNotNull(message);
    assertEquals("Welcome", message);
  }

  @Test
  void getMessageWithReplacements() {
    String message = messageRegistry.get("welcome.user", "Carlos");
    assertNotNull(message);
    assertEquals("Bem-vindo, Carlos", message);
  }

  @Test
  void getMessageWithSpecificLocaleAndReplacements() {
    Locale locale = new Locale("en", "US");
    ResourceBundle bundle = PropertyResourceBundle.getBundle("messages", locale);
    messageRegistry.registerMessages(bundle);
    String message = messageRegistry.get(locale, "welcome.user", "Carlos");
    assertNotNull(message);
    assertEquals("Welcome, Carlos", message);
  }

  @Test
  void getMessageThrowsExceptionForUnknownKey() {
    assertThrows(NullPointerException.class, () -> messageRegistry.get("unknown.key"));
  }

  @Test
  void getMessageThrowsExceptionForUnknownLocale() {
    Locale locale = new Locale("fr", "FR");
    assertThrows(IllegalArgumentException.class, () -> messageRegistry.get(locale, "welcome.message"));
  }

  @Test
  void unregisterMessagesRemovesBundle() {
    Locale locale = new Locale("en", "US");
    ResourceBundle bundle = PropertyResourceBundle.getBundle("messages", locale);
    messageRegistry.registerMessages(bundle);
    messageRegistry.unregisterMessages(bundle);
    assertThrows(NullPointerException.class, () -> messageRegistry.get(locale, "welcome.message"));
  }

  @Test
  void getFormattedMessage() {
    String message = MessageRegistry.getFormattedMessage("welcome.user", "Carlos");
    assertNotNull(message);
    assertEquals("Bem-vindo, Carlos", message);
  }
}