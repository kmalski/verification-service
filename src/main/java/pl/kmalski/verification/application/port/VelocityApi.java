package pl.kmalski.verification.application.port;

import java.time.Duration;

public interface VelocityApi {

    int count(String customerId, Duration window);

}
