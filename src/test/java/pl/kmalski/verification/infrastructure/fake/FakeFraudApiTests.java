package pl.kmalski.verification.infrastructure.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FakeFraudApiTests {

    @Mock
    private LatencySimulator latencySimulator;

    @InjectMocks
    private FakeFraudApi fraudApi;

    @Test
    void shouldReturnOkForSmallPayments() {
        var result = fraudApi.check(payment("9999.99"));

        assertThat(result).isEqualTo(FraudApi.FraudResult.OK);
        then(latencySimulator).should().simulateLatency();
    }

    @Test
    void shouldReturnSuspectedForPaymentsAboveSuspectedThreshold() {
        var result = fraudApi.check(payment("10000.00"));

        assertThat(result).isEqualTo(FraudApi.FraudResult.SUSPECTED);
        then(latencySimulator).should().simulateLatency();
    }

    @Test
    void shouldReturnConfirmedForPaymentsAboveConfirmedThreshold() {
        var result = fraudApi.check(payment("100000.00"));

        assertThat(result).isEqualTo(FraudApi.FraudResult.CONFIRMED);
        then(latencySimulator).should().simulateLatency();
    }

    private static PaymentData payment(String amount) {
        return new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal(amount)),
                new Currency("PLN"),
                new Country("PL")
        );
    }

}
