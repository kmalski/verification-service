package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;

@Component
interface LatencySimulator {
    void simulateLatency();
}
