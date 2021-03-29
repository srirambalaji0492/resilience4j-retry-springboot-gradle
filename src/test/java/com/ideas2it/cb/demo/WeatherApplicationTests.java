package com.ideas2it.cb.demo;


import com.ideas2it.cb.demo.controller.WeatherController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(controllers = WeatherController.class)
class WeatherApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testWeather() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/weather/get/bombay")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String result1 = result.getResponse().getContentAsString();
        assertNotNull(result1);

    }
}
