package common;

public final class Constants {

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private Constants() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static final String GET_URL = "http://httpbin.org/get";
    public static final String POST_URL = "http://httpbin.org/post";
    public static final String PUT_URL = "http://httpbin.org/put";
    public static final String PATCH_URL = "http://httpbin.org/patch";
    public static final String DELETE_URL = "http://httpbin.org/delete";
    public static final String PATCH_METHOD = "PATCH";
    public static final long TIMEOUT_IN_MILI_SECONF = 1000;

}
