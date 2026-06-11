package pl.kmalski.verification.infrastructure.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VerificationRegistrationService;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.Verification;

@Component
@RequiredArgsConstructor
class InMemoryVerificationRegistrationService implements VerificationRegistrationService {

    private final InMemoryVerificationStore store;

    @Override
    public VerificationRegistration registerForPayment(PaymentData payment) {
        var candidate = Verification.start(payment);
        return store.register(candidate);
    }

}
