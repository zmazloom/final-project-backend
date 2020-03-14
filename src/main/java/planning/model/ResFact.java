package planning.model;

import org.springframework.http.HttpStatus;

public class ResFact<T> {

    private Result restResponse;

    private ResFact(Result restResponse) {
        this.restResponse = restResponse;
    }

    public static <T> ResFact<T> build() {
        return new ResFact<T>(new Result<T>());
    }

    public ResFact<T> setResult(T result) {
        this.restResponse.setResult(result);
        return this;
    }

    public ResFact<T> setError(boolean hasError) {
        this.restResponse.setError(hasError);
        return this;
    }

    public ResFact<T> setMessage(String message) {
        this.restResponse.setMessage(message);
        return this;
    }

    public ResFact<T> setCode(HttpStatus status) {
        this.restResponse.setCode(status.value());
        return this;
    }

    public Result<T> get() {
        return restResponse;
    }
}
