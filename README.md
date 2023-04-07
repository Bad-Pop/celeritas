# ![logo](assets/celeritas-black-no_bg-64x64.png) Celeritas

Celeritas is a library written in Java 17 offering several tools to accelerate development by delegating a number of recurring tasks to it.
The word celeritas comes from Latin and means celerity, velocity or speed.

## Why was this library created and why use it ?

When developing software, we all sometimes repeat certain actions recurrently. For example, taking care of JSON serialization/deserialization for our http
requests or improving the performance of our Spring Data JPA applications by fixing the `HHH000104` Hibernate's warning.

All of these small tasks, usually quite quick, but still tedious, are actually common and necessary.
But it still takes some time, so why not "automate" these actions ? That's what Celeritas offers ! Plus, if you're into functional programming, you'll enjoy
using Vavr with Celeritas.

## What features are currently offered by Celeritas ?

- An HTTP client built on top of the client introduced in java 11 which will allow you to save a lot of time by delegating many repetitive tasks such as
  serialization and deserialization of JSON data.
- A custom repository base class for your JPA repositories that will help you deal with the Hibernate HHH000104 firstResult maxResults warning using Spring Data
  JPA Specification and Criteria API.
- A placeholder formatter for java strings.

:heart: **If you would like to see new features in Celeritas, please feel free to submit your ideas or create a pull request**

## How to use Celeritas ?

Simply get the celeritas dependencies with your favourite dependency manager

Maven

```xml
<!-- Celeritas HTTP Client dependency -->
<dependency>
  <groupId>io.github.bad-pop</groupId>
  <artifactId>celeritas-http-client</artifactId>
  <version>x.x.x</version>
</dependency>

<!-- Celeritas Spring Boot 2 Utils dependency -->
<dependency>
  <groupId>io.github.bad-pop</groupId>
  <artifactId>celeritas-sb2-utils</artifactId>
  <version>x.x.x</version>
</dependency>

<!-- Celeritas Spring Boot 3 Utils dependency -->
<dependency>
  <groupId>io.github.bad-pop</groupId>
  <artifactId>celeritas-sb3-utils</artifactId>
  <version>x.x.x</version>
</dependency>

<!-- Celeritas Placeholder dependency -->
<dependency>
<groupId>io.github.bad-pop</groupId>
<artifactId>celeritas-placeholders</artifactId>
<version>x.x.x</version>
</dependency>
```

Gradle

```groovy
// Celeritas HTTP Client dependency
implementation 'io.github.bad-pop:celeritas-http-client:x.x.x'

// Celeritas Spring Boot 2 Utils dependency
implementation 'io.github.bad-pop:celeritas-sb2-utils:x.x.x'

// Celeritas Spring Boot 3 Utils dependency
implementation 'io.github.bad-pop:celeritas-sb3-utils:x.x.x'

// Celeritas Placeholders dependency
implementation 'io.github.bad-pop:celeritas-placeholders:x.x.x'
```

## Documentation
Discover the documentation of the celeritas modules

- [Celeritas Http Client](./doc/celeritas-http-client/README.md)
- [Celeritas Spring Utils](./doc/celeritas-sb-utils/README.md)
- [Celeritas Placeholders](./doc/celeritas-placeholders/README.md)
