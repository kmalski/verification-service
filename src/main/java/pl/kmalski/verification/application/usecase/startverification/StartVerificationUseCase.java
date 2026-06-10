package pl.kmalski.verification.application.usecase.startverification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.orchestration.VerificationWorkflow;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartVerificationUseCase {

    private final VerificationRepository repository;
    private final VerificationWorkflow workflow;

    public StartVerificationResult start(StartVerificationCommand command) {
        var payment = command.payment();
        var verification = Verification.start(payment);

        log.info("Starting verification {} for payment {}", verification.getId(), payment.paymentId());
        repository.save(verification);

        workflow.start(verification.getId());

        log.info("Verification {} queued for processing", verification.getId());
        return new StartVerificationResult(verification.getId(), verification.getStatus());
    }

}
