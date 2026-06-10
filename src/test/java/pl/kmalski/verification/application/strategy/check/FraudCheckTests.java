package pl.kmalski.verification.application.strategy.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.FraudApi;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FraudCheckTests {

    @Mock
    private FraudApi fraudApi;

    @InjectMocks
    private FraudCheck fraudCheck;

    @Test
    void shouldExposeType() {
        assertThat(fraudCheck.type()).isEqualTo(VerificationCheckType.FRAUD);
    }

    @Test
    void shouldPassWhenApiReturnsOk() {
        var payment = validPaymentData();
        given(fraudApi.check(payment)).willReturn(FraudApi.FraudResult.OK);

        var result = fraudCheck.execute(payment);

        assertThat(result).isEqualTo(VerificationCheckResult.passed(VerificationCheckType.FRAUD));
    }

    @Test
    void shouldRequireReviewWhenApiReturnsSuspected() {
        var payment = validPaymentData();
        given(fraudApi.check(payment)).willReturn(FraudApi.FraudResult.SUSPECTED);

        var result = fraudCheck.execute(payment);

        assertThat(result.status()).isEqualTo(VerificationCheckStatus.REQUIRES_REVIEW);
        assertThat(result.reason()).isEqualTo("Fraud risk");
    }

    @Test
    void shouldFailWhenApiReturnsConfirmed() {
        var payment = validPaymentData();
        given(fraudApi.check(payment)).willReturn(FraudApi.FraudResult.CONFIRMED);

        var result = fraudCheck.execute(payment);

        assertThat(result.status()).isEqualTo(VerificationCheckStatus.FAILED);
        assertThat(result.reason()).isEqualTo("Fraud detected");
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
