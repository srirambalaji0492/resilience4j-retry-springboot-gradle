package com.ideas2it.cb.demo.config;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class WeatherConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "retryRegistry")
    public RetryRegistry getRetryRegistry(){
        return RetryRegistry.of(getRetryConfig());
    }

    private RetryConfig getRetryConfig() {

        return RetryConfig.custom()
                .maxAttempts(15)
                .waitDuration(Duration.ofSeconds(2))
                .retryExceptions(RuntimeException.class, UnknownHostException.class, NoRouteToHostException.class)
                .build();
    }

    @Bean(name = "circuitBreakerRegistry")
    public CircuitBreakerRegistry getCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(getCircuitBreakerConfig());
    }

    private CircuitBreakerConfig getCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(100.0f)
                .recordExceptions(ResourceAccessException.class, UnknownHostException.class, RuntimeException.class)
                .minimumNumberOfCalls(10)
                .build();

    }


}
