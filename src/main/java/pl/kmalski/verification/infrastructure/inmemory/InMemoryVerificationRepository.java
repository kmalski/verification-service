package pl.kmalski.verification.infrastructure.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class InMemoryVerificationRepository implements VerificationRepository {

    private final InMemoryVerificationStore store;

    @Override
    public Optional<Verification> findById(VerificationId verificationId) {
        return store.findById(verificationId);
    }

    @Override
    public void save(Verification verification) {
        store.save(verification);
    }

}
