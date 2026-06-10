package pl.kmalski.verification.infrastructure.web;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationQuery;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationCommand;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.domain.model.*;
import pl.kmalski.verification.infrastructure.web.GetVerificationResponse.VerificationCheckResponse;

import java.util.UUID;

@Component
class VerificationDtoMapper {

    public StartVerificationCommand toStartVerificationCommand(StartVerificationRequest request) {
        var payment = request.payment();
        var paymentData = new PaymentData(
                new PaymentId(payment.paymentId()),
                new CustomerId(payment.customerId()),
                new Amount(payment.amount()),
                new Currency(payment.currency()),
                new Country(payment.country())
        );
        return new StartVerificationCommand(paymentData);
    }

    public StartVerificationResponse toStartVerificationResponse(StartVerificationResult result) {
        return new StartVerificationResponse(result.verificationId().value(), result.status());
    }

    public GetVerificationQuery toGetVerificationQuery(UUID verificationId) {
        return new GetVerificationQuery(new VerificationId(verificationId));
    }

    public GetVerificationResponse toGetVerificationResponse(GetVerificationResult result) {
        return new GetVerificationResponse(
                result.verificationId().value(),
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
