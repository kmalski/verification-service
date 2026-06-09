package pl.kmalski.verification.application.usecase.getverification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.exception.VerificationNotFound;

@Service
@RequiredArgsConstructor
public class GetVerificationUseCase {

    private final VerificationRepository verificationRepository;

    public GetVerificationResult get(GetVerificationQuery query) {
        var verification = verificationRepository.findById(query.verificationId())
                .orElseThrow(() -> new VerificationNotFound(query.verificationId()));

        return GetVerificationResult.builder()
                .build();
    }

}
