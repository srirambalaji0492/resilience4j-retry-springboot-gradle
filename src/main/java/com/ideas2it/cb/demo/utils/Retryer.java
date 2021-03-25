package com.ideas2it.cb.demo.utils;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class Retryer {

    @Autowired
    private RetryRegistry  retryRegistry;

    public <T> T retrySupllier(Supplier<T> function){
        Retry retry = retryRegistry.retry("function");
        Supplier<T> retrySupplier = Retry.decorateSupplier(retry, function);
        return retrySupplier.get();
    }
}
