package pl.kmalski.verification.domain.event;

import pl.kmalski.verification.domain.model.VerificationId;

public record VerificationCompletedEvent(VerificationId verificationId) {
}
