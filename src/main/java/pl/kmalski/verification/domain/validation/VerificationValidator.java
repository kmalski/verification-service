package pl.kmalski.verification.domain.validation;

import pl.kmalski.verification.domain.exception.InvalidVerificationException;

public final class VerificationValidator {

    private VerificationValidator() {
    }

    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new InvalidVerificationException(name + " cannot be null");
        }

        return value;
    }

}
