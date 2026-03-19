package org.mxs.hotelapi.controller;

import org.junit.jupiter.api.Test;
import org.mxs.hotelapi.HotelApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HotelApiApplication.class)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeededHotelsList() throws Exception {
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$[0].address", containsString("Belarus")));
    }

    @Test
    void shouldReturnHistogramByCity() throws Exception {
        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Minsk", notNullValue()))
                .andExpect(jsonPath("$.Moscow", notNullValue()))
                .andExpect(jsonPath("$.Warsaw", notNullValue()));
    }

    @Test
    void shouldCreateNewHotel() throws Exception {
        String requestBody = """
                {
                  "name": "Test Hotel",
                  "description": "Test description",
                  "brand": "TestBrand",
                  "address": {
                    "houseNumber": 1,
                    "street": "Test Street",
                    "city": "TestCity",
                    "country": "TestCountry",
                    "postCode": "000000"
                  },
                  "contacts": {
                    "phone": "+000 00 000-00-00",
                    "email": "test@example.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.address", containsString("TestCity")));
    }


    @Test
    void shouldReturnHotelById() throws Exception {
        mockMvc.perform(get("/property-view/hotels/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.address.city", not(isEmptyOrNullString())));
    }

    @Test
    void shouldSearchHotelsByCityAndBrand() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .param("brand", "Hilton"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("Hilton")))
                .andExpect(jsonPath("$[0].address", containsString("Minsk")));
    }

    @Test
    void shouldAddAmenitiesToHotel() throws Exception {
        String requestBody = """
            [
              "Free parking",
              "Free WiFi",
              "Non-smoking rooms"
            ]
            """;

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.amenities", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$.amenities", hasItem("Free WiFi")));
    }


}
