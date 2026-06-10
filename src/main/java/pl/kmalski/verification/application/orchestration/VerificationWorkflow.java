package pl.kmalski.verification.application.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.domain.policy.VerificationDecisionPolicy;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationWorkflow {

    private final VerificationRepository repository;
    private final VerificationDecisionPolicy decisionPolicy;
    private final VerificationOrchestrator orchestrator;

    @Async
    public void start(VerificationId verificationId) {
        try {
            var verification = repository.findById(verificationId)
                    .orElseThrow(() -> new IllegalStateException("Could not find verification with id " + verificationId));

            verification.markInProgress();
            repository.save(verification);

            var results = orchestrator.execute(verification.getPayment());
            var decision = decisionPolicy.decide(results);

            verification.complete(decision, results);
            repository.save(verification);
        } catch (Exception exc) {
            repository.findById(verificationId).ifPresentOrElse(
                    verification -> updateFailed(verification, exc),
                    () -> logFailure(verificationId, exc)
            );
        }
    }

    private void updateFailed(Verification verification, Exception exc) {
        verification.markFailed(exc.getMessage());
        repository.save(verification);
    }

    private void logFailure(VerificationId verificationId, Exception exc) {
        log.error("Verification checks failed, and original verification with id {} cannot be found", verificationId, exc);
    }

}
