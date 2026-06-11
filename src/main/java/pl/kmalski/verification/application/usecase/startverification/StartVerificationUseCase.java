package pl.kmalski.verification.application.usecase.startverification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.orchestration.VerificationWorkflow;
import pl.kmalski.verification.application.port.VerificationRegistrationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartVerificationUseCase {

    private final VerificationRegistrationService registrationService;
    private final VerificationWorkflow workflow;

    public StartVerificationResult start(StartVerificationCommand command) {
        var payment = command.payment();

        var registration = registrationService.registerForPayment(payment);
        var verification = registration.verification();

        if (registration.created()) {
            log.info("Starting verification {} for payment {}", verification.getId(), payment.paymentId());
            workflow.start(verification.getId());
            log.info("Verification {} queued for processing", verification.getId());
        } else {
            log.info("Returning existing verification {} for payment {}", verification.getId(), payment.paymentId());
        }

        return new StartVerificationResult(verification.getId(), verification.getStatus());
    }

}
