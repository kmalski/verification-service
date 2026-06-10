package pl.kmalski.verification.infrastructure.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(VerificationProperties.class)
@Configuration(proxyBeanMethods = false)
class PropertiesConfiguration {}
