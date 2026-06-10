package pl.kmalski.verification.application.usecase.startverification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.orchestration.VerificationOrchestrator;
import pl.kmalski.verification.application.orchestration.VerificationWorkflow;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.policy.VerificationDecisionPolicy;

@Service
@RequiredArgsConstructor
public class StartVerificationUseCase {

    private final VerificationRepository repository;
    private final VerificationWorkflow workflow;

    public StartVerificationResult start(StartVerificationCommand command) {
        var payment = command.payment();
        var verification = Verification.start(payment);

        repository.save(verification);

        workflow.start(verification.getId());

        return new StartVerificationResult(verification.getId());
    }

}
