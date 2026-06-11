package pl.kmalski.verification.application.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VerificationRepository;
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
        var verification = repository.findById(verificationId)
                .orElseThrow(() -> new IllegalStateException("Could not find verification with id " + verificationId));

        log.info("Starting workflow for verification {}", verificationId);
        verification.markInProgress();
        repository.save(verification);

        try {
            var results = orchestrator.execute(verification.getPayment());
            var decision = decisionPolicy.decide(results);

            verification.complete(decision, results);
            repository.save(verification);
            log.info("Verification {} completed with decision {}", verificationId, decision);
        } catch (Exception exc) {
            verification.markFailed(failureReason(exc));
            repository.save(verification);
            log.error("Verification {} failed", verificationId, exc);
        }
    }

    private String failureReason(Exception exc) {
        return exc.getMessage() != null ? exc.getMessage() : exc.getClass().getSimpleName();
    }

}
