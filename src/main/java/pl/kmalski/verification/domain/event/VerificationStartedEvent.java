package pl.kmalski.verification.domain.event;

import pl.kmalski.verification.domain.model.VerificationId;

public record VerificationStartedEvent(VerificationId verificationId) {
}
