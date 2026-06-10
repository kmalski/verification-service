package pl.kmalski.verification.infrastructure.web;

import pl.kmalski.verification.domain.model.VerificationCheckStatus;
import pl.kmalski.verification.domain.model.VerificationCheckType;
import pl.kmalski.verification.domain.model.VerificationDecision;
import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.List;
import java.util.UUID;

record GetVerificationResponse(UUID verificationId,
                               VerificationStatus status,
                               VerificationDecision decision,
                               List<VerificationCheckResponse> checks) {

    record VerificationCheckResponse(VerificationCheckType type,
                                     VerificationCheckStatus status,
                                     String reason) {}

}
