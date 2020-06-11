import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Request;
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
        final String url = "http://httpbin.org/get";

        try {

            // GET Request
            final String getResponseBody = getStringHttpResponse(url).body();
            final Response getResponse = getResponseObject(getResponseBody, Response.class);
            System.out.println("get response: " + getResponse);

            // POST request
            final String postUrl = "http://httpbin.org/post";
            final String postRequest = getRequestBodyAsString(postUrl);
            final String postResponseBody = postMethod("http://httpbin.org/post", postRequest).body();
            final Response postResponse = getResponseObject(postResponseBody, Response.class);
            System.out.println("post response: " + postResponse);

            // PUT request
            final String putUrl = "http://httpbin.org/put";
            final String putRequest = getRequestBodyAsString(putUrl);
            final String putResponseBody = putMethod(putUrl, putRequest).body();
            final Response putResponse = getResponseObject(putResponseBody, Response.class);
            System.out.println("put response: " + putResponse);

            // PATCH request
            final String patchUrl = "http://httpbin.org/patch";
            final String patchRequest = getRequestBodyAsString(patchUrl);
            final String patchResponseBody = patchMethod(patchUrl, patchRequest).body();
            final Response patchResponse = getResponseObject(patchResponseBody, Response.class);
            System.out.println("patch response: " + patchResponse);

            // DELETE request
            System.out.println("delete response: " + deleteMethod("http://httpbin.org/delete").body());

        }  catch (JsonProcessingException e) {
            System.out.println("exception " + e);
            throw new RuntimeException("error");
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }

        // Multiple request
        List<URI> ui = new ArrayList();
        ui.add(URI.create(url));
        ui.add(URI.create(url));
        System.out.println("multiple request: ");
        getURIs(ui);

        Thread.sleep(10000);
        System.out.println("output: ");

    }

    private static <T> T getResponseObject(final String getResponseBody,final Class<T> clazz)
            throws JsonProcessingException {
        return objectMapper.readValue(getResponseBody, clazz);
    }

    private static String getRequestBodyAsString(final String url) throws JsonProcessingException {
        Request putRequest = Request.builder().origin("112.196.145.87").url(url).build();
        return objectMapper.writeValueAsString(putRequest);
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

    private static HttpResponse<String> patchMethod(final String patchUrl, final String requestBody) throws Exception {

        // There is no direct method for PATCH so we use the method() in which first arguement
        // is the URL method that we should provide in the ALL CAPS. Second argument in is BODY.
        HttpRequest request = HttpRequest.newBuilder(URI.create(patchUrl))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
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

    private static HttpResponse<String> putMethod(final String putUrl, final String requestBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(putUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());

    }

    public static void getURIs(List<URI> uris) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
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