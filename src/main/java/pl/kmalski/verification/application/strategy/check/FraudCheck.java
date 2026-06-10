package pl.kmalski.verification.application.strategy.check;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import static pl.kmalski.verification.domain.model.VerificationCheckType.FRAUD;

@Component
@RequiredArgsConstructor
public class FraudCheck implements VerificationCheck {

    private final FraudApi fraudApi;

    @Override
    public VerificationCheckType type() {
        return FRAUD;
    }

    @Override
    public VerificationCheckResult execute(PaymentData payment) {
        var result = fraudApi.check(payment);

        return switch (result) {
            case OK -> VerificationCheckResult.passed(FRAUD);
            case SUSPECTED -> VerificationCheckResult.requireReview(FRAUD, "Fraud risk");
            case CONFIRMED -> VerificationCheckResult.failed(FRAUD, "Fraud detected");
        };
    }

}
