package pl.kmalski.verification.infrastructure.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

record StartVerificationRequest(@Valid @NotNull Payment payment) {

    record Payment(@NotBlank @Size(max = 128) String paymentId,
                   @NotBlank @Size(max = 128) String customerId,
                   @NotNull @Positive BigDecimal amount,
                   @NotBlank @Pattern(regexp = "[A-Za-z]{3}", message = "Currency must be a valid ISO 4217 code") String currency,
                   @NotBlank @Pattern(regexp = "[A-Za-z]{2}", message = "Country must be a valid ISO 3166-1 alpha-2 code") String country) {}

}
