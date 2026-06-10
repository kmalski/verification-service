package pl.kmalski.verification.application.strategy.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.SanctionsApi;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SanctionsCheckTests {

    @Mock
    private SanctionsApi sanctionsApi;

    @InjectMocks
    private SanctionsCheck sanctionsCheck;

    @Test
    void shouldExposeType() {
        assertThat(sanctionsCheck.type()).isEqualTo(VerificationCheckType.SANCTIONS);
    }

    @Test
    void shouldPassWhenCountryIsNotSanctioned() {
        var payment = validPaymentData();
        given(sanctionsApi.isCountrySanctioned(payment.country())).willReturn(false);

        var result = sanctionsCheck.execute(payment);

        assertThat(result).isEqualTo(VerificationCheckResult.passed(VerificationCheckType.SANCTIONS));
    }

    @Test
    void shouldFailWhenCountryIsSanctioned() {
        var payment = validPaymentData();
        given(sanctionsApi.isCountrySanctioned(payment.country())).willReturn(true);

        var result = sanctionsCheck.execute(payment);

        assertThat(result.status()).isEqualTo(VerificationCheckStatus.FAILED);
        assertThat(result.reason()).isEqualTo("Sanctioned country");
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
