package pl.kmalski.verification.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import pl.kmalski.verification.domain.exception.VerificationNotFound;

@RestControllerAdvice
public class VerificationExceptionHandler {

    @ExceptionHandler(VerificationNotFound.class)
    public ProblemDetail handleVerificationNotFound(VerificationNotFound exception, HttpServletRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Verification not found");
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ProblemDetail handleBadRequest(Exception exception, HttpServletRequest request) {
        var detail = exception.getMessage() != null ? exception.getMessage() : "Invalid request";
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Bad request");
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }

}
