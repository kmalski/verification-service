package pl.kmalski.verification.application.usecase.startverification;

import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.UUID;

public record StartVerificationResult(UUID verificationId,
                                     VerificationStatus status) {}
