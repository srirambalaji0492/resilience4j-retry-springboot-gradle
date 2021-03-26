package com.ideas2it.cb.demo.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CircuitBreaker {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public <T> T breakCircuit(Supplier<T> function) {
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("function");
        Supplier<T> breakedSupplier = circuitBreaker.decorateSupplier(function);
        return breakedSupplier.get();
    }
}
