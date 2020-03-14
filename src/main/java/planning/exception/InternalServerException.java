package planning.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }


    public static InternalServerException getInstance() {
        return new InternalServerException("internal server error!");

    }

    public static InternalServerException getInstance(String msg) {
        return new InternalServerException(msg);

    }

}
