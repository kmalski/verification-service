package pl.kmalski.verification.application.usecase.getverification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.exception.VerificationNotFound;

@Service
@RequiredArgsConstructor
public class GetVerificationUseCase {

    private final VerificationRepository repository;

    public GetVerificationResult get(GetVerificationQuery query) {
        var verificationId = query.verificationId();

        var verification = repository.findById(verificationId)
                .orElseThrow(() -> new VerificationNotFound(verificationId));

        return new GetVerificationResult();
    }

}
