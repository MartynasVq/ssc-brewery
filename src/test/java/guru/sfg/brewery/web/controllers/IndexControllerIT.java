package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
//Add SpringBootTest when using our custom user details service as mvctest does minimal config
@SpringBootTest
public class IndexControllerIT extends BaseIT{

    @Test
    void indexPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }
}
