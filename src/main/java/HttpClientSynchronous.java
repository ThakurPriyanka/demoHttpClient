import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Constants;
import common.Utils;
import domain.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientSynchronous {

    private static HttpClient client;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        client = HttpClient.newHttpClient();

        try {

            // GET_URL Request
            final String getResponseBody = getStringHttpResponse(Constants.GET_URL).body();
            final Response getResponse = Utils.getResponseInObject(getResponseBody, Response.class);
            System.out.println("get response: " + getResponse);

            // POST request
            final String postUrl = "http://httpbin.org/post";
            final String postRequest = Utils.getRequestBodyAsString(Constants.POST_URL);
            final String postResponseBody = postMethod("http://httpbin.org/post", postRequest).body();
            final Response postResponse = Utils.getResponseInObject(postResponseBody, Response.class);
            System.out.println("post response: " + postResponse);

            // PUT request
            final String putUrl = "http://httpbin.org/put";
            final String putRequest = Utils.getRequestBodyAsString(Constants.PUT_URL);
            final String putResponseBody = putMethod(Constants.PUT_URL, putRequest).body();
            final Response putResponse = Utils.getResponseInObject(putResponseBody, Response.class);
            System.out.println("put response: " + putResponse);

            // PATCH request
            final String patchUrl = "http://httpbin.org/patch";
            final String patchRequest = Utils.getRequestBodyAsString(Constants.PATCH_URL);
            final String patchResponseBody = patchMethod(Constants.PATCH_URL, patchRequest).body();
            final Response patchResponse = Utils.getResponseInObject(patchResponseBody, Response.class);
            System.out.println("patch response: " + patchResponse);

            // DELETE request
            System.out.println("delete response: " + deleteMethod(Constants.DELETE_URL).body());

        } catch (JsonProcessingException e) {
            System.out.println("exception " + e);
            throw new RuntimeException("error");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        // Multiple request
        List<URI> uriList = new ArrayList();
        uriList.add(URI.create(Constants.GET_URL));
        uriList.add(URI.create(Constants.GET_URL));
        System.out.println("multiple request: ");
        multipleRequest(uriList);

    }

    private static HttpResponse<String> getStringHttpResponse(final String url) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder(URI.create(url))
                        .GET()
                        .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> postMethod(final String postUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> putMethod(final String putUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(putUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());

    }

    private static HttpResponse<String> patchMethod(final String patchUrl, final String requestBody) throws Exception {

        // There is no direct method for PATCH so we use the method() in which first arguement
        // is the URL method that we should provide in the ALL CAPS. Second argument in is BODY.

        HttpRequest request = HttpRequest.newBuilder(URI.create(patchUrl))
                .header("Content-Type", "application/json")
                .method(Constants.PATCH_METHOD, HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> deleteMethod(final String deleteUrl) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(deleteUrl))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void multipleRequest(List<URI> uris) throws IOException {

        List<HttpRequest> requests = uris.stream()
                .map(HttpRequest::newBuilder)
                .map(reqBuilder -> reqBuilder.build())
                .collect(Collectors.toList());

        requests.stream()
                .map(request -> {
                    try {
                        return client.send(request, HttpResponse.BodyHandlers.ofString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .forEach(response -> System.out.println("response body" + response.body()));
    }
}