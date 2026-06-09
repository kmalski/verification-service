package pl.kmalski.verification.application.usecase.startverification;

import lombok.Builder;
import pl.kmalski.verification.domain.model.PaymentId;

import static java.util.Objects.requireNonNull;

@Builder
public record StartVerificationCommand(PaymentId paymentId) {

    public StartVerificationCommand {
        requireNonNull(paymentId, "Payment id must not be null");
    }

}
