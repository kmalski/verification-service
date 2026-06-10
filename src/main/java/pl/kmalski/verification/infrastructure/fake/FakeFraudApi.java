package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.Amount;
import pl.kmalski.verification.domain.model.PaymentData;

import static pl.kmalski.verification.infrastructure.fake.LatencySimulator.simulateLatency;

@Component
public class FakeFraudApi implements FraudApi {

    private static final Amount CONFIRMED_THRESHOLD = Amount.valueOf("99999.99");
    private static final Amount SUSPECTED_THRESHOLD = Amount.valueOf("9999.99");

    @Override
    public FraudResult check(PaymentData payment) {
        simulateLatency();

        return switch (payment.amount()) {
            case Amount amount when amount.greaterThan(CONFIRMED_THRESHOLD) -> FraudResult.CONFIRMED;
            case Amount amount when amount.greaterThan(SUSPECTED_THRESHOLD) -> FraudResult.SUSPECTED;
            default -> FraudResult.OK;
        };
    }

}
