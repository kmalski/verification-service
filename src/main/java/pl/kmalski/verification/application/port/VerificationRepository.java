package pl.kmalski.verification.application.port;

import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;

import java.util.Optional;

public interface VerificationRepository {

    Optional<Verification> findById(VerificationId verificationId);

    void save(Verification verification);

}
