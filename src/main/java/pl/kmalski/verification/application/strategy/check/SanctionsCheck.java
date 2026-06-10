package pl.kmalski.verification.application.strategy.check;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.SanctionsApi;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

@Component
@RequiredArgsConstructor
public class SanctionsCheck implements VerificationCheck {

    private final SanctionsApi sanctionsApi;

    @Override
    public VerificationCheckType type() {
        return VerificationCheckType.SANCTIONS;
    }

    @Override
    public VerificationCheckResult execute(PaymentData payment) {
        boolean isCountrySanctioned = sanctionsApi.isCountrySanctioned(payment.country());

        return isCountrySanctioned
                ? VerificationCheckResult.failed(type(), "Sanctioned country")
                : VerificationCheckResult.passed(type());
    }

}
