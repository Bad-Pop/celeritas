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

:heart: **If you would like to see new features in Celeritas, please feel free to submit your ideas or create a pull request**

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

## The Celeritas http client

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

Now that you have your http client, you can prepare your request. Nothing could be easier. Create an `java.net.http.HttpRequest` as you would do with the
classic java client.

```java
HttpRequest request = HttpRequest.newBuilder()
  .uri(uri)
  .GET()
  .build();
BodyHandler<String> responseBodyHandler = myHttpClient.createStringBodyHandler();

CeleritasHttpResponse<String> response = myHttpClient.send(request, responseBodyHandler);
```

Or if you want to set a body in your request :

```java
BodyPublisher requestBody = myHttpClient.createJsonBodyPublisher(objectToSerializeIntoJson);
HttpRequest request = HttpRequest.newBuilder()
  .uri(uri)
  .POST(requestBody)
  .build();
BodyHandler<String> responseBodyHandler = myHttpClient.createStringBodyHandler();

CeleritasHttpResponse<String> response = myHttpClient.send(request, responseBodyHandler);
```

As you can see in this example, the Celeritas client allows you to serialize a java object into json very easily and get a BodyPublisher containing the
resulting
JSON. But there are many other methods available through the CeleritasHttpClient API. For example :

```java
Try<BodyPublisher> maybebodyPublisher = myHttpClient.tryToCreateJsonBodyPublisher(objectToSerializeIntoJson);
BodyPublisher fileBodyPublisher = myHttpClient.createFileBodyPublisher(filePath);
```

Also note that you can send asynchronous requests with the `sendAsync()` and `sendAsyncAsFuture()` methods in the Celeritas client or safe synchronous requests
with the `tryToSend()` method.

#### Working with responses

Once you have sent a request, the client returns a `CeleritasHttpResponse<T>` where `T` represents the type of the response body.
CeleritasHttpResponse represents an http response. This class is built on top of `java.net.http.HttpResponse` introduced in Java 11 and is fully interoperable
with it. But the CeleritasHttpResponse gives you access to a more complete API with many methods to simplify your work. Finally, it gives access to methods
based on the Vavr library for those who wish to develop with very powerful functional interfaces.

For example, if you want to deserialize the response body (in JSON) to an object of type MyObject, then you can use this method :

```java
CeleritasHttpResponse<String> response = myHttpClient.send(request,bodyHandler);
MyObject deserilizedJsonResponseBody = response.readBody(MyObject.class);

//Or if MyObject is a generic you can call this method
CeleritasHttpResponse<String> response = myHttpClient.send(request,bodyHandler);
MyObject<String> deserilizedJsonResponseBody = response.readBody(new TypeReference<MyObject<String>>(){});
```

CeleritasHttpResponse also gives you the option to resend the request that generated this response. For this, several methods are available:

```java
CeleritasHttpResponse<T> retry();
Try<CeleritasHttpResponse<T>> tryToRetry();
CompletableFuture<CeleritasHttpResponse<T>> retryAsync();
Future<CeleritasHttpResponse<T>> retryAsyncAsFuture();
```

Finally, if you don't want to work with a CeleritasHttpResponse but with a native `java.net.http.HttpResponse`, you can simply call the `toJavaResponse()`
method :

```java
HttpResponse<String> nativeResponse = myCeleritasHttpResponse.toJavaResponse();
```

---

To get a better overview of the features offered by the Celeritas http client, feel free to look at the javadoc

## Spring boot related features

Celeritas offers features that integrate with your spring boot applications.
Celeritas `SB2` versions support spring boot version `2.x` and `SB3` versions support spring boot version `3.x`.
But if you don't use Spring boot, no worries, you can always use Celeritas and select any version.

### The SimpleCeleritasJpaRepository repository base class

The main purpose of the SimpleCeleritasJpaRepository class is to allow you to solve the `HHH000104` firstResult maxResults warning using Spring Data JPA
Specification and Criteria API. Indeed, whenever you use pagination and SQL joins to fetch entities and their associations to prevent the N+1 queries problem,
you'll most-likely run into the Hibernate's `HHH000104` warning message.

:sos: _**Be aware that this alert is very bad for your application, and especially for its performance when your dataset grows.**_

The SimpleCeleritasJpaRepository class implements the solution to this problem that was proposed by the excellent Vlad Mihalcea
in [this article](https://vladmihalcea.com/fix-hibernate-hhh000104-entity-fetch-pagination-warning-message/).

#### How to use SimpleCeleritasJpaRepository ?

To use SimpleCeleritasJpaRepository only a few steps are required. First, you need to add this configuration to your Spring Boot application :

```java
@EnableJpaRepositories(repositoryBaseClass = SimpleCeleritasJpaRepository.class)
```

Then make your repository extends CeleritasJpaSpecificationExecutor and override the findAll method to use an `@EntityGraph` :

```java

@Repository
public interface MyRepository extends JpaRepository<EntityClass, Long>, CeleritasJpaSpecificationExecutor<EntityClass, Long> {

  @Override
  @EntityGraph(attributePaths = "yourJoinAttribute")
  List<EntityClass> findAll(Specification<EntityClass> spec);
}

//Or more quickly using the Celeritas GenericRepository
@Repository
public interface MyRepository extends GenericRepository<EntityClass, Long> {

  @Override
  @EntityGraph(attributePaths = "yourJoinAttribute")
  List<EntityClass> findAll(Specification<EntityClass> spec);
}
```

Then you simply call the `findAllEntitiesIds()` method from your own repositories, which will return the IDs of the entities matching your search.
Once you have retrieved the ids of your entities, create a new JPA specification using the `idIn()` method.

```java
// Please note that the specification can be null here
Page<Long> myEntitiesIdsPage = myRepository.findAllEntitiesIds(specification,pageable);

List<EntityClass> result;
List<Long> ids = myEntitiesIdsPage.getContent();

if (CollectionUtils.isEmpty(ids)) {
  result = Collections.emptyList();
} else {
  // Retrieve entities using IN predicate
  Specification<EntityClass> idInSpecification = myRepository.idIn(ids, "myEntityIdAttributeName");
  result = myRepository.findAll(idInSpecification);
}

return PageableExecutionUtils
    .getPage(result, pageable, ()-> myEntitiesIdsPage.getTotalElements());
```

With this approach, you effectively eliminate the performance problem caused by paging in your application memory by relaying it to your database.

#### Conclusion on the Hibernate `HHH000104` warning

The `HHH000104` warning is so bad. There is no SQL pagination because the pagination is done in the application memory. Imagine that your query retrieves 1 or 2
million elements from your database without pagination in the SQL query actually sent : you then load the 1 or 2 million elements into your application and
return only a small part to the caller... **This is counterproductive !**

There a couple of approaches to fix the pagination in memory. One of them is to execute two SQL queries. The first query retrieves just the entities’ ID
using pagination and the second query retrieves the entities using an IN predicate with the IDs from the first query. This is the solution offered by Celeritas
with SimpleCeleritasJpaRepository.
