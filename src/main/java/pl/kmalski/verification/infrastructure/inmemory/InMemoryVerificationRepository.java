package pl.kmalski.verification.infrastructure.inmemory;

import org.springframework.stereotype.Repository;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
class InMemoryVerificationRepository implements VerificationRepository {

    private final Map<VerificationId, Verification> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Verification> findById(VerificationId verificationId) {
        return Optional.ofNullable(store.get(verificationId));
    }

    @Override
    public void save(Verification verification) {
        store.put(verification.getId(), verification);
    }

}
