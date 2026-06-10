package pl.kmalski.verification.application.usecase.startverification;

import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.UUID;

public record StartVerificationResult(VerificationId verificationId,
                                      VerificationStatus status) {}
