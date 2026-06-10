package pl.kmalski.verification.application.usecase.getverification;

import pl.kmalski.verification.domain.model.VerificationId;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record GetVerificationQuery(VerificationId verificationId) {

    public GetVerificationQuery {
        requireNonNull(verificationId, "Verification id");
    }

}
