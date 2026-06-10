package pl.kmalski.verification.domain.exception;

public class InvalidVerificationException extends IllegalArgumentException {

    public InvalidVerificationException(String message) {
        super(message);
    }

}
