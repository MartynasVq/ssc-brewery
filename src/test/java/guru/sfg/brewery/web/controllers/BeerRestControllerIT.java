package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/67f8242b-5f11-49a6-a05e-05ac6e56e281")
                .header("api-key", "admin2").header("api-secret", "admin2"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/67f8242b-5f11-49a6-a05e-05ac6e56e281")).andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036")).andExpect(status().isOk());
    }

}
