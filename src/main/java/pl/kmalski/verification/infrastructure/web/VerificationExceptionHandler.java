package pl.kmalski.verification.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;
import pl.kmalski.verification.domain.exception.VerificationNotFoundException;

@Slf4j
@RestControllerAdvice
class VerificationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleInternalServerError(Exception exception,
                                                   HttpServletRequest request) {
        log.error("Exception caught for request: {} {}", request.getMethod(), request.getRequestURI(), exception);
        var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        return problem;
    }

    @ExceptionHandler(VerificationNotFoundException.class)
    public ProblemDetail handleNotFound(VerificationNotFoundException exception,
                                        HttpServletRequest request) {
        log.info("Exception caught for request: {} {}", request.getMethod(), request.getRequestURI(), exception);
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Not found");
        return problem;
    }

    @ExceptionHandler({
            InvalidVerificationException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ProblemDetail handleBadRequest(Exception exception,
                                          HttpServletRequest request) {
        log.info("Exception caught for request: {} {}", request.getMethod(), request.getRequestURI(), exception);
        var detail = exception.getMessage() != null ? exception.getMessage() : "Invalid request";
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Bad request");
        return problem;
    }

}
