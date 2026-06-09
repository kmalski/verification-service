package pl.kmalski.verification.application.strategy.check;

import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

public interface VerificationCheck {

    VerificationCheckType type();

    VerificationCheckResult execute(PaymentData paymentData);

}
