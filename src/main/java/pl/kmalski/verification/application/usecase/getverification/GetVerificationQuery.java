package pl.kmalski.verification.application.usecase.getverification;

import pl.kmalski.verification.domain.model.VerificationId;

import static java.util.Objects.requireNonNull;

public record GetVerificationQuery(VerificationId verificationId) {

    public GetVerificationQuery {
        requireNonNull(verificationId);
    }

}
