package com.ideas2it.cb.demo.controller;

import com.ideas2it.cb.demo.utils.CircuitBreaker;
import com.ideas2it.cb.demo.utils.Constants;
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
    private int counter = 0;
    private final int total = 15;

    public WeatherController(RestTemplate restTemplate, CircuitBreaker cb) {
        this.restTemplate = restTemplate;
        this.cb = cb;
    }

    @GetMapping(path = "/get/{location}")
    public ResponseEntity getWeatherForLocation(@PathVariable(name = "location", required = true) String location){

        String originalResp;

        Supplier<String> function = () -> getWeatherForCity(location);
        //originalResp =  retryer.retrySupllier(function);
        originalResp = cb.breakCircuit(function);
        System.out.println(originalResp);

        return ResponseEntity.ok(originalResp);
    }


    private String getWeatherForCity(String cityName){
        checkandThrow();
        String url = String.format("%s%s%s%s%s" , Constants.API_WEATHER, "?q=",cityName, "&appid=", Constants.API_KEY );
        System.out.println("url : " + url);
        String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
        System.out.println("Response : " + response);
        return response;
    }

    private void checkandThrow(){
        System.out.println("Exception counter : " +counter + " Total : " + total);
        if(counter < total){
            counter++;
            throw new RuntimeException("Counter is : " +counter);
        }
    }
}
