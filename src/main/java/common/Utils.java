package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Request;

public final class Utils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private Utils() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static <T> T getResponseInObject(final String response, final Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(response, clazz);
        } catch (JsonProcessingException e) {
            System.out.println("exception " + e);
            throw new RuntimeException("error");
        }
    }

    public static String getRequestBodyAsString(final String url) throws JsonProcessingException {
        Request putRequest = Request.builder().origin("112.196.145.87").url(url).build();
        return objectMapper.writeValueAsString(putRequest);
    }
}
