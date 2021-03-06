package com.ideas2it.cb.demo.controller;

import com.ideas2it.cb.demo.utils.CircuitBreaker;
import com.ideas2it.cb.demo.utils.Constants;
import com.ideas2it.cb.demo.utils.Retryer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@RestController
@RequestMapping(path = "/weather")
public class WeatherController {

    private final RestTemplate restTemplate;
    private final CircuitBreaker cb;
    private final Retryer retryer;
    private int counter = 0;

    public WeatherController(RestTemplate restTemplate, CircuitBreaker cb, Retryer retryer) {
        this.restTemplate = restTemplate;
        this.cb = cb;
        this.retryer = retryer;
    }

    @GetMapping(path = "/get/{location}")
    public ResponseEntity getWeatherForLocation(@PathVariable(name = "location") String location) {

        String originalResp;

        Supplier<String> function = () -> getWeatherForCity(location);
        Supplier<String> breakedCIrcuitFunction = cb.breakCircuitwithoutGet(function);
        originalResp = retryer.retrySupllier(breakedCIrcuitFunction);

        System.out.println(originalResp);

        return ResponseEntity.ok(originalResp);
    }


    private String getWeatherForCity(String cityName){
        //checkandThrow();
        String url = String.format("%s%s%s%s%s" , Constants.API_WEATHER, "?q=",cityName, "&appid=", Constants.API_KEY );
        System.out.println("url : " + url);
        String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
        System.out.println("Response : " + response);
        return response;
    }

    private void checkandThrow(){
        int total = 15;
        System.out.println("Exception counter : " +counter + " Total : " + total);
        if(counter < total){
            counter++;
            throw new RuntimeException("Counter is : " +counter);
        }
    }
}
