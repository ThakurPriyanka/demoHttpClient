import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.*;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import domain.Requestss;
import domain.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class HttpClientASynchronous {

    private static HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws Exception {

        client = HttpClient.newHttpClient();
        String url = "http://httpbin.org/get";

        // final Requestss request = Requestss.builder().origin("112.196.145.87").url("http://httpbin.org/post").build();
        final Requestss request = Requestss.builder().price(3455).roomNumber("456").build();

 /*       //Get Requestss
        getStringHttpResponse(url)
                .thenAccept(s -> System.out.println("get response: " + s)); */

        //Post request
        //try {
        System.out.println("ss: " + request);
        try {
            String s = objectMapper.writeValueAsString(request);
            System.out.println("requeste " + s);

        postMethod("http://localhost:8080/services/roomservice/api/rooms", s)
                .thenAccept(jsonString -> {
                    System.out.println("post response1323: " + jsonString);
                    try {
                        Room response = new ObjectMapper().readValue(jsonString, Room.class);
                        System.out.println("response: " + response);
                    } catch (JsonProcessingException e) {
                        System.out.println("exception " + e);
                        throw new RuntimeException("error");
                    }
                }).exceptionally(exception -> {

            System.out.println("exception " + exception);
            throw new RuntimeException("error");
        });
    }  catch(JsonProcessingException e) {
            System.out.println("exception " + e);
            throw new RuntimeException("error");
        }
        //PAtch request
 /*         patchMethod("http://httpbin.org/patch")
                .thenAccept(s -> System.out.println("put response: " + s));
      List<URI> ui = new ArrayList();
        ui.add(URI.create(url));
        ui.add(URI.create(url));
        getURIs(ui);*/
        Thread.sleep(1000);
        System.out.println("output: " );
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
              .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU5MTYyNjI1MH0.mTp7p1V4i9teNWmy1IAM_Jq5pVexaGCFjz6cxGsUKk8p6dVIsGeDQxj3N3KTyrbKYe6JGwHZw335Q5Y4W5Y1zA")
              .POST(BodyPublishers.ofString(requestBody))
              .build();
        CompletableFuture<String> stringCompletableFuture = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
        System.out.println("post " + request);
        System.out.println("stringCompletableFuture " + stringCompletableFuture);
        return stringCompletableFuture;
  }

  private static CompletableFuture<String> patchMethod(final String patchUrl) throws Exception {

      HttpRequest request = HttpRequest.newBuilder(URI.create(patchUrl))
              .header("Content-Type", "application/json")
              .method("PATCH", BodyPublishers.ofString("{\n" +
                      " \n" +
                      "  \"json\": null,\n" +
                      "  \"origin\": \"112.196.145.87\",\n" +
                      "  \"url\": \"http://httpbin.org/patch\"\n" +
                      "}"))
              .build();

      System.out.println("request " + request);

      return client
              .sendAsync(request, HttpResponse.BodyHandlers.ofString())
               .thenApply(HttpResponse::body);
  }

    public static void getURIs(List<URI> uris) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        List<HttpRequest> requests = uris.stream()
                .map(HttpRequest::newBuilder)
                .map(reqBuilder -> reqBuilder.build())
                .collect(Collectors.toList());

/*        List<CompletableFuture<String>> futures = new ArrayList();

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(requests.stream()
                .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .toArray(CompletableFuture<?>[]::new));

        voidCompletableFuture.theAccept( ignored -> s.stream() .map(CompletableFuture::join)
                .filter(Optional::isPresent).forEach(p -> System.out.println("p " + p)));*/
    }
}
