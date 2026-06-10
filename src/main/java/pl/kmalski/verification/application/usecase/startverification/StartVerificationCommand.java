package pl.kmalski.verification.application.usecase.startverification;

import lombok.Builder;
import pl.kmalski.verification.domain.model.PaymentData;

import static java.util.Objects.requireNonNull;

@Builder
public record StartVerificationCommand(PaymentData payment) {

    public StartVerificationCommand {
        requireNonNull(payment);
    }

}
