package planning.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import planning.modelVO.ApiErrorVO;
import java.util.Calendar;

@ControllerAdvice(basePackages = "planning")
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value =
            {RuntimeException.class,
                    InvalidRequestException.class,
                    ResourceNotFoundException.class,
                    ResourceConflictException.class,
                    InternalServerException.class})
    public final ResponseEntity<?> handleExceptions(RuntimeException ex, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof InvalidRequestException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleExceptionInternal(ex, headers, status, request);
        } else if (ex instanceof ResourceConflictException) {
            HttpStatus status = HttpStatus.CONFLICT;
            return handleExceptionInternal(ex, headers, status, request);
        } else if (ex instanceof ResourceNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleExceptionInternal(ex, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, headers, status, request);
        }
    }

    /**
     * A single place to customize the response body of all Exception types.
     *
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex      The exception
     * @param headers The headers for the response
     * @param status  The response status
     * @param request The current request
     */
    private ResponseEntity<ApiErrorVO> handleExceptionInternal(Exception ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        ApiErrorVO body = ApiErrorVO.builder()
                .timestamp(Calendar.getInstance().getTime())
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .status(status.value())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();


        return new ResponseEntity<>(body, headers, status);
    }

}