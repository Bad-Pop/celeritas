# The Celeritas http client

CeleritasHttpClient is an http client built on top of the native `java.net.http.HttpClient` introduced in Java 11.
This client is fully based on the java 11 client but gives you access to a more complete API with many methods to simplify your work.
It also relies on the Jackson library and its famous `com.fasterxml.jackson.databind.ObjectMapper` to provide serialization and deserialization features
directly from the client. Finally, it gives access to methods based on the Vavr library for those who wish to develop with very powerful functional interfaces.

## How to use it ?

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

### Working with responses

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
