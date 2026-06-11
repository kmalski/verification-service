package pl.kmalski.verification.application.port;

import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.Verification;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public interface VerificationRegistrationService {

    VerificationRegistration registerForPayment(PaymentData payment);

    record VerificationRegistration(Verification verification, boolean created) {}

}
