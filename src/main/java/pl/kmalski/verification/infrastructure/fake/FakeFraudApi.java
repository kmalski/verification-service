package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.PaymentData;

import java.time.Duration;

@Component
public class FakeFraudApi implements FraudApi {

    @Override
    public FraudResult check(PaymentData payment) {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return FraudResult.OK;
    }

}
