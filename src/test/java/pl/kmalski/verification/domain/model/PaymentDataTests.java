package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentDataTests {

    @Test
    void shouldCreatePaymentDataWhenArgumentsAreValid() {
        var paymentData = new PaymentData(
                "payment-1",
                "customer-1",
                new BigDecimal("12.50"),
                "PLN",
                "PL"
        );

        assertThat(paymentData.paymentId()).isEqualTo("payment-1");
        assertThat(paymentData.customerId()).isEqualTo("customer-1");
        assertThat(paymentData.amount()).isEqualByComparingTo("12.50");
        assertThat(paymentData.currency()).isEqualTo("PLN");
        assertThat(paymentData.country()).isEqualTo("PL");
    }

    @Test
    void shouldRejectNonPositiveAmount() {
        assertThatThrownBy(() -> new PaymentData(
                "payment-1",
                "customer-1",
                BigDecimal.ZERO,
                "PLN",
                "PL"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Payment amount must be positive");
    }

    @Test
    void shouldRejectNullArguments() {
        assertThatThrownBy(() -> new PaymentData(null, "customer-1", new BigDecimal("1.00"), "PLN", "PL"))
                .isInstanceOf(NullPointerException.class);
    }
}
