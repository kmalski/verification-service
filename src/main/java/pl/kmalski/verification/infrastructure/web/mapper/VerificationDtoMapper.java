package pl.kmalski.verification.infrastructure.web.mapper;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationQuery;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationCommand;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.infrastructure.web.dto.GetVerificationResponse;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationRequest;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationResponse;

import java.util.UUID;

@Component
public class VerificationDtoMapper {

    public StartVerificationCommand toStartVerificationCommand(StartVerificationRequest request) {
        return StartVerificationCommand.builder()
                .build();
    }

    public StartVerificationResponse toStartVerificationResponse(StartVerificationResult result) {
        return StartVerificationResponse.builder()
                .build();
    }

    public GetVerificationQuery toGetVerificationQuery(UUID verificationId) {
        return GetVerificationQuery.builder()
                .build();
    }

    public GetVerificationResponse toGetVerificationResponse(GetVerificationResult result) {
        return GetVerificationResponse.builder()
                .build();
    }

}
