package pl.kmalski.verification.infrastructure.web;

import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.UUID;

record StartVerificationResponse(UUID verificationId,
                                 VerificationStatus status) {}
