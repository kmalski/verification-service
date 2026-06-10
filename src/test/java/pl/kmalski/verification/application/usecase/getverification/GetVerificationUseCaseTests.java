package pl.kmalski.verification.application.usecase.getverification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.exception.VerificationNotFound;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationId;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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
        var verificationId = VerificationId.random();
        var verification = Verification.start(validPaymentData());
        given(repository.findById(verificationId)).willReturn(Optional.of(verification));

        var result = useCase.get(new GetVerificationQuery(verificationId));

        then(repository).should().findById(verificationId);
        assertThat(result).isNotNull();
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
