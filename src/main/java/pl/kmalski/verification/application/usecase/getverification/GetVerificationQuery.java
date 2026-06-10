package pl.kmalski.verification.application.usecase.getverification;

import lombok.Builder;
import pl.kmalski.verification.domain.model.VerificationId;

import static java.util.Objects.requireNonNull;

@Builder
public record GetVerificationQuery(VerificationId verificationId) {

    public GetVerificationQuery {
        requireNonNull(verificationId);
    }

}
