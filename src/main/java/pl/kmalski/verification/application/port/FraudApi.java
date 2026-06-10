package pl.kmalski.verification.application.port;

import pl.kmalski.verification.domain.model.PaymentData;

public interface FraudApi {

    FraudResult check(PaymentData payment);

    enum FraudResult {
        OK,
        SUSPECTED,
        CONFIRMED
    }

}
