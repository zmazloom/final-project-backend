package planning.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }


    public static InvalidRequestException getInstance() {
        return new InvalidRequestException("invalid request!");

    }

    public static InvalidRequestException getInstance(String msg) {
        return new InvalidRequestException(msg);

    }

}
