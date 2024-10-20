# Translatica

**Translatica** is a lightweight Java library for managing messages and translations using `ResourceBundle`. It allows you to register and retrieve localized messages based on `Locale`, making internationalization of applications easier.

## Installation

### Maven
Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.mtbarr</groupId>
    <artifactId>translatica</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Gradle
Add the following to your `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.mtbarr:translatica:1.0-SNAPSHOT'
}
```

## Usage Example

The main class of the project is `MessageRegistry`, which allows you to register and retrieve messages from properties files. Below are some examples of how to use the library.

### Registering Messages

```java
import io.github.mtbarr.translatica.MessageRegistry;

public class Main {
    public static void main(String[] args) {
        // Create a message registry using the default "messages.properties" file
        MessageRegistry registry = new MessageRegistry();

        // Alternatively, specify a custom properties file
        MessageRegistry customRegistry = new MessageRegistry("customMessages");
    }
}
```

### Retrieving Messages

```java
import io.github.mtbarr.translatica.MessageRegistry;

import java.util.Locale;

public class Main {
  public static void main(String[] args) {
    // Create a registry
    // Creating it with no arguments will use the default "messages.properties" file
    MessageRegistry registry = new MessageRegistry();
    //    MessageRegistry registry = new MessageRegistry("custom_messages");


    // Retrieve a message with the default locale
    String message = registry.get("welcome.message");

    // Retrieve a message for a specific locale
    String messageInFrench = registry.get(Locale.FRENCH, "welcome.message");

    // Retrieve a message with replacements
    String formattedMessage = registry.get("welcome.user", "John");

    System.out.println(message);           // Output: Welcome!
    System.out.println(messageInFrench);   // Output: Bienvenue!
    System.out.println(formattedMessage);  // Output: Welcome, John!
  }
}
```

### Static Access

You can also use the static methods for accessing messages without needing to instantiate `MessageRegistry`:

```java
import io.github.mtbarr.translatica.MessageRegistry;

public class Main {
    public static void main(String[] args) {
        // Retrieve a message statically
        String message = MessageRegistry.getMessage("welcome.message");

        // Retrieve a formatted message statically
        String formattedMessage = MessageRegistry.getFormattedMessage("welcome.user", "John");
        
        System.out.println(message);           // Output: Welcome!
        System.out.println(formattedMessage);  // Output: Welcome, John!
    }
}
```

## Features

- **Locale-based message retrieval**: Get translated messages based on the `Locale`.
- **Message formatting**: Supports message formatting with dynamic replacements.
- **Simple resource bundle registration**: Easily register message bundles from `.properties` files.
- **Static methods**: Access messages without creating an instance of `MessageRegistry`.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
