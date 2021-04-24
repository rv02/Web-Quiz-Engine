package engine;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final String NOT_FOUND_MESSAGE = "No Entry Match for Given Data";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({QuizNotFoundException.class, UsernameNotFoundException.class})
    public HashMap<String, String> handleQuizNotFoundException(Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", NOT_FOUND_MESSAGE);
        response.put("error", e.getClass().getSimpleName());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class,
            org.hibernate.exception.ConstraintViolationException.class})
    public HashMap<String, String> handleValidationExceptions(Exception e) {
        HashMap<String, String> error = new HashMap<>();
        error.put("Reason", "Not Valid Json");
        error.put("error", e.getMessage());
        return error;
    }
}
