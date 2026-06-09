package pl.kmalski.verification.application.usecase.startverification;

import lombok.Builder;
import pl.kmalski.verification.domain.model.VerificationId;

@Builder
public record StartVerificationResult(VerificationId verificationId) {



}
