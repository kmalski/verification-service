package pl.kmalski.verification.domain.exception;

import pl.kmalski.verification.domain.model.VerificationId;

public class VerificationNotFound extends RuntimeException {
    public VerificationNotFound(VerificationId verificationId) {
        super("Verification with id " + verificationId + " not found");
    }
}
