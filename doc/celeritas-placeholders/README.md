# Celeritas placeholders

Celeritas Placeholders is a library for formatting strings with placeholders.
It comes with a default Formatter, but can be extended to create your own.
A placeholder is a default value that must be replaced by a real value at a given time.

## How to use Celeritas Placeholders

### Using the default formatter

The default formatter allows you to work with placeholders in the following format: `[$][{]([\w\d_-]+)}`.
This means that placeholder keys can contain upper and lower case letters, numbers, underscores and dashes.

Here are some examples of valid placeholders:

- `${placeholder}`
- `${myPlaceholder}`
- `${my_placeholder}`
- `${a-more-ComplEx_Pl4ceHolder}`

To get an instance of the default formatter you just have to call the following static method :
```java
PlaceholderFormatter defaultFormatter = PlaceholderFormatter.newFormatter();
```

Then, all you have to do is format your strings containing placeholders using the formatter you have obtained :
```java
String format = "A string containing a ${placeholder}";
Map<String, Object> parameters = Map.of("placeholder", "formatted placeholder");

String formatted = defaultFormatter.format(format, parameters);

assertThat(formatted).isEqualTo("A string containing a formatted placeholder");
```

#### Vavr support

Celeritas Placeholders also allows you to work with functional types provided by the Vavr library.
To do this, you simply instantiate a new `FunctionalPlaceholderFormatter` by calling the following static method:
```java
FunctionalPlaceholderFormatter defaultFormatter = FunctionalPlaceholderFormatter.newFunctionalFormatter();
```

### Create your own formatter

_This section will be available soon..._
