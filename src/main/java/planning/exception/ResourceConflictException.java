package planning.exception;

public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }


    public static ResourceConflictException getInstance() {
        return new ResourceConflictException("resource conflict!");

    }

    public static ResourceConflictException getInstance(String msg) {
        return new ResourceConflictException(msg);

    }
}
