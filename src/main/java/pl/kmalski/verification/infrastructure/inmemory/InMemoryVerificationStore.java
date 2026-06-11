package pl.kmalski.verification.infrastructure.inmemory;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VerificationRegistrationService.VerificationRegistration;
import pl.kmalski.verification.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class InMemoryVerificationStore {

    private final Map<VerificationId, StoredVerification> verificationsById = new ConcurrentHashMap<>();
    private final Map<PaymentId, VerificationId> verificationIdsByPaymentId = new ConcurrentHashMap<>();

    public Optional<Verification> findById(VerificationId verificationId) {
        return Optional.ofNullable(verificationsById.get(verificationId))
                .map(StoredVerification::toDomain);
    }

    public void save(Verification verification) {
        verificationsById.put(verification.getId(), StoredVerification.fromDomain(verification));
        verificationIdsByPaymentId.put(verification.getPaymentId(), verification.getId());
    }

    public VerificationRegistration register(Verification candidate) {
        var storedId = verificationIdsByPaymentId.computeIfAbsent(
                candidate.getPaymentId(),
                _ -> {
                    verificationsById.put(candidate.getId(), StoredVerification.fromDomain(candidate));
                    return candidate.getId();
                }
        );
        var stored = verificationsById.get(storedId);

        if (stored == null) {
            throw new IllegalStateException("Could not find verification with id " + storedId);
        }

        return new VerificationRegistration(stored.toDomain(), stored.id().equals(candidate.getId()));
    }

    private record StoredVerification(VerificationId id,
                                      PaymentData payment,
                                      VerificationStatus status,
                                      VerificationDecision decision,
                                      String failureReason,
                                      List<VerificationCheckResult> checkResults) {

        static StoredVerification fromDomain(Verification verification) {
            return new StoredVerification(
                    verification.getId(),
                    verification.getPayment(),
                    verification.getStatus(),
                    verification.getDecision(),
                    verification.getFailureReason(),
                    verification.getCheckResults()
            );
        }

        Verification toDomain() {
            return Verification.builder()
                    .id(id)
                    .payment(payment)
                    .status(status)
                    .decision(decision)
                    .failureReason(failureReason)
                    .checkResults(new ArrayList<>(checkResults))
                    .build();
        }

    }

}
