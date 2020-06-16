import common.Constants;
import common.Utils;
import domain.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpClientASynchronous {

    private static HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {

        // GET_URL Request
        getStringHttpResponse(Constants.GET_URL).thenAccept(response -> {
            final Response responseInObject = Utils.getResponseInObject(response, Response.class);
            System.out.println("Get Response: " + responseInObject);
        });

        // POST request
        final String postRequest = Utils.getRequestBodyAsString(Constants.POST_URL);
        postMethod("http://httpbin.org/post", postRequest).thenAccept(response -> {
            final Response responseInObject = Utils.getResponseInObject(response, Response.class);
            System.out.println("Post Response: " + responseInObject);
        });

        // PUT request
        final String putRequest = Utils.getRequestBodyAsString(Constants.PUT_URL);
        putMethod(Constants.PUT_URL, putRequest).thenAccept(response -> {
            final Response responseInObject = Utils.getResponseInObject(response, Response.class);
            System.out.println("Put Response: " + responseInObject);
        });

        // PATCH request
        final String patchRequest = Utils.getRequestBodyAsString(Constants.PATCH_URL);
        patchMethod(Constants.PATCH_URL, patchRequest).thenAccept(response -> {
            final Response responseInObject = Utils.getResponseInObject(response, Response.class);
            System.out.println("Patch Response: " + responseInObject);
        });

        // DELETE request
        deleteMethod(Constants.DELETE_URL).thenAccept(response -> {
            final Response responseInObject = Utils.getResponseInObject(response, Response.class);
            System.out.println("Delete Response: " + responseInObject);
        });

        // If you are not able to see the time please increase the sleep timeout time.
        Thread.sleep(Constants.TIMEOUT_IN_MILI_SECONF);
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
                .method(Constants.PATCH_METHOD, BodyPublishers.ofString(requestBody))
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
}
