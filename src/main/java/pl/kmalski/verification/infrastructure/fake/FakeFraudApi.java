package pl.kmalski.verification.infrastructure.fake;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.Amount;
import pl.kmalski.verification.domain.model.PaymentData;

@Component
@RequiredArgsConstructor
class FakeFraudApi implements FraudApi {

    private static final Amount CONFIRMED_THRESHOLD = Amount.valueOf("99999.99");
    private static final Amount SUSPECTED_THRESHOLD = Amount.valueOf("9999.99");

    private final LatencySimulator latencySimulator;

    @Override
    public FraudResult check(PaymentData payment) {
        latencySimulator.simulateLatency();

        return switch (payment.amount()) {
            case Amount amount when amount.greaterThan(CONFIRMED_THRESHOLD) -> FraudResult.CONFIRMED;
            case Amount amount when amount.greaterThan(SUSPECTED_THRESHOLD) -> FraudResult.SUSPECTED;
            default -> FraudResult.OK;
        };
    }

}
