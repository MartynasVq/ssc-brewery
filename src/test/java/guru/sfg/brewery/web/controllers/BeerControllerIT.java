package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
//Add SpringBootTest when using our custom user details service as mvctest does minimal config
@SpringBootTest
public class BeerControllerIT extends BaseIT{


    @WithMockUser("admin")
    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/beers/find/").with(httpBasic("user", "user"))).andExpect(status().isOk()).andExpect(view()
                .name("beers/findBeers")).andExpect(model().attributeExists("beer"));
    }

    @Test
    void testHttpBasicAuth() throws Exception {

        mockMvc.perform(get("/beers/find/").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk()).andExpect(view()
                .name("beers/findBeers")).andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersPermissionTest() throws Exception {
        mockMvc.perform(get("/beers/find")).andExpect(status().isUnauthorized());
    }

}
