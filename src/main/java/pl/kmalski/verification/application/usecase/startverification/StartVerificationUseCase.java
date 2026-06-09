package pl.kmalski.verification.application.usecase.startverification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.orchestration.VerificationOrchestrator;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.Verification;

@Service
@RequiredArgsConstructor
public class StartVerificationUseCase {

    private final VerificationRepository verificationRepository;
    private final VerificationOrchestrator verificationOrchestrator;

    public StartVerificationResult start(StartVerificationCommand command) {
        var verification = Verification.create();

        verificationRepository.save(verification);

        return StartVerificationResult.builder()
                .verificationId(verification.getId())
                .build();
    }

}
