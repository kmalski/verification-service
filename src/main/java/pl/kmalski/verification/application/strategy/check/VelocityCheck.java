package pl.kmalski.verification.application.strategy.check;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VelocityApi;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

@Component
@RequiredArgsConstructor
public class VelocityCheck implements VerificationCheck {

    private final VelocityApi velocityApi;
    private final VerificationConfiguration configuration;

    @Override
    public VerificationCheckType type() {
        return VerificationCheckType.VELOCITY;
    }

    @Override
    public VerificationCheckResult execute(PaymentData payment) {
        int count = velocityApi.count(payment.customerId(), configuration.getVelocityWindow());

        if (count > configuration.getVelocityLimit()) {
            return VerificationCheckResult.failed(type(), "High velocity");
        }

        return VerificationCheckResult.passed(type());
    }

}
