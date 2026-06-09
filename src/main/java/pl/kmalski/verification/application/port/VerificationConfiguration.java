package pl.kmalski.verification.application.port;

import java.time.Duration;

public interface VerificationConfiguration {

    Duration getVerificationCheckTimeout();

}
