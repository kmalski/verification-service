package pl.kmalski.verification.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kmalski.verification.domain.policy.VerificationDecisionPolicy;

@Configuration(proxyBeanMethods = false)
class DomainConfiguration {

    @Bean
    public VerificationDecisionPolicy verificationDecisionPolicy() {
        return new VerificationDecisionPolicy();
    }

}
