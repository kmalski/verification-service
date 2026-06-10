package pl.kmalski.verification.application.strategy.check;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

@Component
@RequiredArgsConstructor
public class FraudCheck implements VerificationCheck {

    private final FraudApi fraudApi;

    @Override
    public VerificationCheckType type() {
        return VerificationCheckType.FRAUD;
    }

    @Override
    public VerificationCheckResult execute(PaymentData payment) {
        var result = fraudApi.check(payment);

        return switch (result) {
            case OK -> VerificationCheckResult.passed(type());
            case SUSPECTED -> VerificationCheckResult.requireReview(type(), "Fraud risk");
            case CONFIRMED -> VerificationCheckResult.failed(type(), "Fraud detected");
        };
    }

}
