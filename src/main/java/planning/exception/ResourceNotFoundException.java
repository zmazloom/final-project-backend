package planning.exception;

public class ResourceNotFoundException extends RuntimeException {

    private String resource;

    public ResourceNotFoundException(String resource) {
        this.resource = resource;
    }

    public static ResourceNotFoundException getInstance(String msg) {
        return new ResourceNotFoundException(msg);
    }

    @Override
    public String getMessage() {
        return resource;
    }
}
