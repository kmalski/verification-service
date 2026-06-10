package pl.kmalski.verification.application.strategy.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VelocityApi;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VelocityCheckTests {

    @Mock
    private VelocityApi velocityApi;

    @Mock
    private VerificationConfiguration configuration;

    @InjectMocks
    private VelocityCheck velocityCheck;

    @Test
    void shouldExposeType() {
        assertThat(velocityCheck.type()).isEqualTo(VerificationCheckType.VELOCITY);
    }

    @Test
    void shouldPassWhenVelocityIsWithinLimit() {
        var payment = validPaymentData();
        given(velocityApi.count(payment.customerId().value(), Duration.ofMinutes(10))).willReturn(3);
        given(configuration.getVelocityWindow()).willReturn(Duration.ofMinutes(10));
        given(configuration.getVelocityLimit()).willReturn(5);

        var result = velocityCheck.execute(payment);

        assertThat(result).isEqualTo(VerificationCheckResult.passed(VerificationCheckType.VELOCITY));
    }

    @Test
    void shouldFailWhenVelocityExceedsLimit() {
        var payment = validPaymentData();
        given(configuration.getVelocityWindow()).willReturn(Duration.ofMinutes(10));
        given(configuration.getVelocityLimit()).willReturn(5);
        given(velocityApi.count(payment.customerId().value(), Duration.ofMinutes(10))).willReturn(6);

        var result = velocityCheck.execute(payment);

        assertThat(result.status()).isEqualTo(VerificationCheckStatus.FAILED);
        assertThat(result.reason()).isEqualTo("High velocity");
    }

    private static PaymentData validPaymentData() {
        return new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("10.00")),
                new Currency("PLN"),
                new Country("PL")
        );
    }
}
