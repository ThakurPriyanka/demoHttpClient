import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class HttpClientSynchronous {
    private static HttpClient client;

    public static void main(String[] args) throws Exception {

        client = HttpClient.newHttpClient();
        String url = "http://httpbin.org/get";

        //GET Requestss
        System.out.println("get response: " + getStringHttpResponse(url).body());

        //POST request
        System.out.println("post response: " + postMethod("http://httpbin.org/post").body());

        //PUT request
        System.out.println("put response: " + putMethod("http://httpbin.org/put").body());

        //PATCH request
        System.out.println("patch response: " + patchMethod("http://httpbin.org/patch").body());

        //DELETE request
        System.out.println("delete response: " + deleteMethod("http://httpbin.org/delete").body());

        // Multiple request
        List<URI> ui = new ArrayList();
        ui.add(URI.create(url));
        ui.add(URI.create(url));
        System.out.println("multiple request: ");
        getURIs(ui);

        Thread.sleep(1000);
        System.out.println("output: ");
    }

    private static HttpResponse<String> getStringHttpResponse(final String url) throws Exception {

        HttpRequest request =
                HttpRequest.newBuilder(URI.create(url))
                        .GET()
                        .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> postMethod(final String postUrl) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(postUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                        " \n" +
                        "  \"json\": null,\n" +
                        "  \"origin\": \"112.196.145.87\",\n" +
                        "  \"url\": \"http://httpbin.org/post\"\n" +
                        "}"))
                .build();

        return client
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> patchMethod(final String patchUrl) throws Exception {

        // There is no direct method for PATCH so we use the method() in which first arguement
        // is the URL method that we should provide in the ALL CAPS. Second argument in is BODY.
        HttpRequest request = HttpRequest.newBuilder(URI.create(patchUrl))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\n" +
                        " \n" +
                        "  \"json\": null,\n" +
                        "  \"origin\": \"112.196.145.87\",\n" +
                        "  \"url\": \"http://httpbin.org/post\"\n" +
                        "}"))
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

    private static HttpResponse<String> putMethod(final String putUrl) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(putUrl))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\n" +
                        " \n" +
                        "  \"json\": null,\n" +
                        "  \"origin\": \"112.196.145.87\",\n" +
                        "  \"url\": \"http://httpbin.org/post\"\n" +
                        "}"))
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