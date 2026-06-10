package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentDataTests {

    @Test
    void shouldCreatePaymentDataWhenArgumentsAreValid() {
        var paymentData = new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("12.50")),
                new Currency("PLN"),
                new Country("PL")
        );

        assertThat(paymentData.paymentId()).isEqualTo(new PaymentId("payment-1"));
        assertThat(paymentData.customerId()).isEqualTo(new CustomerId("customer-1"));
        assertThat(paymentData.amount()).isEqualTo(new Amount(new BigDecimal("12.50")));
        assertThat(paymentData.currency()).isEqualTo(new Currency("PLN"));
        assertThat(paymentData.country()).isEqualTo(new Country("PL"));
    }

    @Test
    void shouldRejectNullArguments() {
        var customerId = new CustomerId("customer-1");
        var amount = new Amount(new BigDecimal("12.50"));
        var currency = new Currency("PLN");
        var country = new Country("PL");

        assertThatThrownBy(() -> new PaymentData(null, customerId, amount, currency, country))
                .isInstanceOf(InvalidVerificationException.class)
                .hasMessage("Payment id cannot be null");
    }
}
