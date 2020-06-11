import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Request;
import domain.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpClientASynchronous {

    private static HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        //client = HttpClient.newHttpClient();

            //GET Request
            final String url = "http://httpbin.org/get";
            getStringHttpResponse(url).thenAccept(response -> {
                final Response responseInObject = getResponseInObject(response, Response.class);
                System.out.println("Get Response: " + responseInObject);
            });

            //POST request
            final String postUrl = "http://httpbin.org/post";
            final String postRequest = getRequestBodyAsString(postUrl);
            postMethod("http://httpbin.org/post", postRequest).thenAccept(response -> {
                final Response responseInObject = getResponseInObject(response, Response.class);
                System.out.println("Post Response: " + responseInObject);
            });

            //PUT request
            final String putUrl = "http://httpbin.org/put";
            final String putRequest = getRequestBodyAsString(putUrl);
            putMethod(putUrl, putRequest).thenAccept(response -> {
                final Response responseInObject = getResponseInObject(response, Response.class);
                System.out.println("Put Response: " + responseInObject);
            });

            //PATCH request
            final String patchUrl = "http://httpbin.org/patch";
            final String patchRequest = getRequestBodyAsString(patchUrl);
            patchMethod(patchUrl, patchRequest).thenAccept(response -> {
                final Response responseInObject = getResponseInObject(response, Response.class);
                System.out.println("Patch Response: " + responseInObject);
            });

            //DELETE request
            deleteMethod("http://httpbin.org/delete").thenAccept(response -> {
                final Response responseInObject = getResponseInObject(response, Response.class);
                System.out.println("Delete Response: " + responseInObject);
            });
        //If you are not able to see the time please increase the sleep timeout time.
        Thread.sleep(1000);
    }


    private static CompletableFuture<String> getStringHttpResponse(final String url) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder(URI.create(url))
                        .GET()
                        .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    private static CompletableFuture<String> postMethod(final String postUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        CompletableFuture<String> stringCompletableFuture = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        return stringCompletableFuture;
    }

    private static CompletableFuture<String> putMethod(final String putUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(putUrl))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(requestBody))
                .build();

        return client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

    }

    private static CompletableFuture<String> patchMethod(final String patchUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(patchUrl))
                .header("Content-Type", "application/json")
                .method("PATCH", BodyPublishers.ofString(requestBody))
                .build();

        return client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    private static CompletableFuture<String> deleteMethod(final String deleteUrl) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(deleteUrl))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    private static <T> T getResponseInObject(final String response, final Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(response, clazz);
        } catch (JsonProcessingException e) {
            System.out.println("exception " + e);
            throw new RuntimeException("error");
        }
    }

    private static String getRequestBodyAsString(final String url) throws JsonProcessingException {
        Request putRequest = Request.builder().origin("112.196.145.87").url(url).build();
        return objectMapper.writeValueAsString(putRequest);
    }
}
