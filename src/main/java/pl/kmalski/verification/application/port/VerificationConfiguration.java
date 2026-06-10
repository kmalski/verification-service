package pl.kmalski.verification.application.port;

import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.time.Duration;
import java.util.Set;

public interface VerificationConfiguration {

    Duration getVerificationCheckTimeout();

    Duration getVelocityWindow();

    int getVelocityLimit();

    Set<VerificationCheckType> getEnabledVerificationCheckTypes();

}
