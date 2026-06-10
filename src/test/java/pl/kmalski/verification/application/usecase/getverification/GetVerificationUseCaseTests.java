package pl.kmalski.verification.application.usecase.getverification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.exception.VerificationNotFound;
import pl.kmalski.verification.domain.model.VerificationCheckStatus;
import pl.kmalski.verification.domain.model.VerificationCheckType;
import pl.kmalski.verification.domain.model.VerificationDecision;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.domain.model.VerificationStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class GetVerificationUseCaseTests {

    @Mock
    private VerificationRepository repository;

    @InjectMocks
    private GetVerificationUseCase useCase;

    @Test
    void shouldReturnResultWhenVerificationExists() {
        var verification = Verification.start(validPaymentData());
        var verificationId = verification.getId();
        given(repository.findById(verificationId)).willReturn(Optional.of(verification));

        var result = useCase.get(new GetVerificationQuery(verificationId));

        then(repository).should().findById(verificationId);
        assertThat(result.verificationId()).isEqualTo(verificationId);
        assertThat(result.status()).isEqualTo(VerificationStatus.QUEUED);
        assertThat(result.decision()).isNull();
        assertThat(result.checkResults()).isEmpty();
    }

    @Test
    void shouldThrowWhenVerificationDoesNotExist() {
        var verificationId = VerificationId.random();
        given(repository.findById(verificationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.get(new GetVerificationQuery(verificationId)))
                .isInstanceOf(VerificationNotFound.class)
                .hasMessageMatching("Verification with id [a-f0-9\\-]+ not found");

        then(repository).should().findById(verificationId);
    }

    private static PaymentData validPaymentData() {
        return new PaymentData(
                "payment-1",
                "customer-1",
                new BigDecimal("10.00"),
                "PLN",
                "PL"
        );
    }
}
