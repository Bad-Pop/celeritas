# ![logo](assets/celeritas-black-no_bg-64x64.png) Celeritas

Celeritas is a library written in Java 17 offering several tools to accelerate development by delegating a number of recurring tasks to it.
The word celeritas comes from Latin and means celerity, velocity or speed.

## How to use Celeritas ?

Simply get the celeritas dependency with your favourite dependency manager

Maven

```xml
<dependency>
  <groupId>com.github.bad-pop</groupId>
  <artifactId>celeritas</artifactId>
  <version>x.x.x</version>
</dependency>
```

Gradle

```groovy
implementation 'com.github.bad-pop:celeritas:x.x.x'
```

## What features are currently offered by Celeritas ?

- An HTTP client built on top of the client introduced in java 11 which will allow you to save a lot of time by delegating many repetitive tasks such as
  serialization and deserialization of JSON data.
- A custom repository base class for your JPA repositories that will help you deal with the Hibernate HHH000104 firstResult maxResults warning using Spring Data
  JPA Specification and Criteria API.

**If you would like to see new features in Celeritas, please feel free to submit your ideas or create a pull request**

## The CeleritasHttpClient

CeleritasHttpClient is an http client built on top of the native `java.net.http.HttpClient` introduced in Java 11.
This client is fully based on the java 11 client but gives you access to a more complete API with many methods to simplify your work.
It also relies on the Jackson library and its famous `com.fasterxml.jackson.databind.ObjectMapper` to provide serialization and deserialization features
directly from the client. Finally, it gives access to methods based on the Vavr library for those who wish to develop with very powerful functional interfaces.

### How to use it ?

First, instantiate a new instance of CeleritasHttpClient. There are several ways to do this:

Build a default http client :
```java
CeleritasHttpClient myHttpClient = CeleritasHttpClient.newClient();
```

Build a new http client with a custom `java.net.http.HttpClient` :
```java
HttpClient myJdkHttpClient = HttpClient.newHttpClient();
CeleritasHttpClient myHttpClient = CeleritasHttpClient.newClient(jdkHttpClient);
```

Build a new http client with a custom `com.fasterxml.jackson.databind.ObjectMapper` :
```java
ObjectMapper om = new ObjectMapper();
CeleritasHttpClient myHttpClient = CeleritasHttpClient.newClient(om);
```

Build a new http client with a custom `java.net.http.HttpClient` and a custom `com.fasterxml.jackson.databind.ObjectMapper`
```java
HttpClient myJdkHttpClient = HttpClient.newHttpClient();
ObjectMapper om = new ObjectMapper();
CeleritasHttpClient myHttpClient = CeleritasHttpClient.newClient(myJdkHttpClient, om);
```

## Spring boot

Celeritas offers features that integrate with your spring boot applications.
Celeritas `SB2` versions support spring boot version `2.x` and `SB3` versions support spring boot version `3.x`.
But if you don't use Spring boot, no worries, you can always use Celeritas and select any version.
