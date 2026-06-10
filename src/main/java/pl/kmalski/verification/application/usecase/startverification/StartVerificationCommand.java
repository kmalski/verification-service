package pl.kmalski.verification.application.usecase.startverification;

import pl.kmalski.verification.domain.model.PaymentData;

import static java.util.Objects.requireNonNull;

public record StartVerificationCommand(PaymentData payment) {

    public StartVerificationCommand {
        requireNonNull(payment);
    }

}
