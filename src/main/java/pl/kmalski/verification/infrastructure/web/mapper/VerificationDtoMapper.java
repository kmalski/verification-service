package pl.kmalski.verification.infrastructure.web.mapper;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationQuery;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationCommand;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.infrastructure.web.dto.GetVerificationResponse;
import pl.kmalski.verification.infrastructure.web.dto.GetVerificationResponse.VerificationCheckResponse;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationRequest;
import pl.kmalski.verification.infrastructure.web.dto.StartVerificationResponse;

import java.util.UUID;

@Component
public class VerificationDtoMapper {

    public StartVerificationCommand toStartVerificationCommand(StartVerificationRequest request) {
        var payment = request.payment();
        var paymentData = new PaymentData(
                payment.paymentId(),
                payment.customerId(),
                payment.amount(),
                payment.currency(),
                payment.country()
        );
        return new StartVerificationCommand(paymentData);
    }

    public StartVerificationResponse toStartVerificationResponse(StartVerificationResult result) {
        return new StartVerificationResponse(result.verificationId(), result.status());
    }

    public GetVerificationQuery toGetVerificationQuery(UUID verificationId) {
        return new GetVerificationQuery(new VerificationId(verificationId));
    }

    public GetVerificationResponse toGetVerificationResponse(GetVerificationResult result) {
        return new GetVerificationResponse(
                result.verificationId().id(),
                result.status(),
                result.decision(),
                result.checkResults().stream()
                        .map(this::toVerificationCheckResponse)
                        .toList()
        );
    }

    private VerificationCheckResponse toVerificationCheckResponse(VerificationCheckResult result) {
        return new VerificationCheckResponse(result.type(), result.status(), result.reason());
    }

}
