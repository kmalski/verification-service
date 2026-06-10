package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

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
        assertThatThrownBy(() -> new PaymentData(null, new CustomerId("customer-1"), new Amount(new BigDecimal("1.00")), new Currency("PLN"), new Country("PL")))
                .isInstanceOf(NullPointerException.class);
    }
}
