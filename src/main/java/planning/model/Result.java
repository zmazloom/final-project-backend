package planning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.net.HttpURLConnection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Result<T> {

    @Builder.Default
    private int code = HttpURLConnection.HTTP_OK;

    @Builder.Default
    private String message = "Ok.";

    private T result;

    private boolean error;

    public Result(T result) {
        this.result = result;
    }

    public Result(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
