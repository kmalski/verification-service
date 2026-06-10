package pl.kmalski.verification.application.usecase.startverification;

import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.domain.model.VerificationStatus;

public record StartVerificationResult(VerificationId verificationId,
                                      VerificationStatus status) {}
