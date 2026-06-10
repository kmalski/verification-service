package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.PaymentData;

@Component
public class FakeFraudApi implements FraudApi {

    @Override
    public FraudResult check(PaymentData payment) {
        return FraudResult.OK;
    }

}
