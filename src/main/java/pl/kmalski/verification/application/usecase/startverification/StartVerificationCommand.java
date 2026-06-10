package pl.kmalski.verification.application.usecase.startverification;

import pl.kmalski.verification.domain.model.PaymentData;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record StartVerificationCommand(PaymentData payment) {

    public StartVerificationCommand {
        requireNonNull(payment, "Payment");
    }

}
