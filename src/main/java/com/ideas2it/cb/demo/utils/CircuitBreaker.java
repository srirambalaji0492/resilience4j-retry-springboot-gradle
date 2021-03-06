package com.ideas2it.cb.demo.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CircuitBreaker {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private int counter = 0;


    public CircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public <T> T breakCircuit(Supplier function) {
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("function");
        System.out.println("circuitBreaker State : " +circuitBreaker.getState().name());
        Supplier<T> breakedSupplier = circuitBreaker.decorateSupplier(function);
        return breakedSupplier.get();
    }

    public Supplier breakCircuitwithoutGet(Supplier function) {
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("function");
        System.out.println("circuitBreaker State : " + circuitBreaker.getState().name());
        return circuitBreaker.decorateSupplier(function);
    }
}
