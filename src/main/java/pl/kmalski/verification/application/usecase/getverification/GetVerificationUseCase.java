package pl.kmalski.verification.application.usecase.getverification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.domain.exception.VerificationNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetVerificationUseCase {

    private final VerificationRepository repository;

    public GetVerificationResult get(GetVerificationQuery query) {
        var verificationId = query.verificationId();

        log.debug("Fetching verification {}", verificationId);
        var verification = repository.findById(verificationId)
                .orElseThrow(() -> new VerificationNotFoundException(verificationId));

        log.debug("Verification {} loaded with status {}", verificationId, verification.getStatus());
        return new GetVerificationResult(
                verification.getId(),
                verification.getStatus(),
                verification.getDecision(),
                verification.getCheckResults().stream()
                        .map(result -> new VerificationCheckResult(result.type(), result.status(), result.reason()))
                        .toList()
        );
    }

}
