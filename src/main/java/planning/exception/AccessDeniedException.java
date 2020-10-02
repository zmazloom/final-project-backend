package planning.exception;

public class AccessDeniedException extends RuntimeException {

    private String resource;

    public AccessDeniedException(String resource) {
        this.resource = resource;
    }

    public static AccessDeniedException getInstance(String msg) {
        return new AccessDeniedException(msg);
    }

    @Override
    public String getMessage() {
        return resource;
    }
}
