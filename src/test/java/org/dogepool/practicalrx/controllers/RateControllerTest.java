package org.dogepool.practicalrx.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.or;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.dogepool.practicalrx.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class RateControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRateEuro() throws Exception {
        mockMvc.perform(get("/rate/EUR").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(startsWith("{\"moneyCodeFrom\":\"DOGE\",\"moneyCodeTo\":\"EUR\",\"exchangeRate\":")));
    }

    @Test
    public void testRateBadCurrencyTooLong() throws Exception {
        //TODO improve
        mockMvc.perform(get("/rate/EURO").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isRequestTimeout());
    }

    @Test
    public void testRateBadCurrencyBadCase() throws Exception {
        //TODO improve
        mockMvc.perform(get("/rate/EuR").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isRequestTimeout());
    }
}