package pl.kmalski.verification.application.usecase.getverification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.domain.exception.VerificationNotFound;
import pl.kmalski.verification.domain.model.Verification;

@Service
@RequiredArgsConstructor
public class GetVerificationUseCase {

    private final VerificationRepository repository;

    public GetVerificationResult get(GetVerificationQuery query) {
        var verificationId = query.verificationId();

        var verification = repository.findById(verificationId)
                .orElseThrow(() -> new VerificationNotFound(verificationId));

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
