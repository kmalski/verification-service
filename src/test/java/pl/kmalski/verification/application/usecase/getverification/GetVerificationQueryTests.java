package pl.kmalski.verification.application.usecase.getverification;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetVerificationQueryTests {

    @Test
    void shouldRejectNullVerificationId() {
        assertThatThrownBy(() -> new GetVerificationQuery(null))
                .isInstanceOf(NullPointerException.class);
    }
}
