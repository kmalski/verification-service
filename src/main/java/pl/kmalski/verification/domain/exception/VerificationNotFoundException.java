package pl.kmalski.verification.domain.exception;

import pl.kmalski.verification.domain.model.VerificationId;

public class VerificationNotFoundException extends RuntimeException {

    public VerificationNotFoundException(VerificationId verificationId) {
        super("Verification with id " + verificationId + " not found");
    }

}
