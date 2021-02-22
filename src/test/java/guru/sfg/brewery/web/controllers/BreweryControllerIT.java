package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BreweryControllerIT extends BaseIT{

    @Test
    void listBreweriesCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesUserRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesAdminRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries").with(httpBasic("admin2", "admin2")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesNotAuthenticated() throws Exception {
        mockMvc.perform(get("/brewery/breweries")).andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesCustomerRoleRest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void listBreweriesUserRoleRest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesNotAuthenticatedRest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")).andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesAdminRoleRest() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries").with(httpBasic("admin2", "admin2")))
                .andExpect(status().isOk());
    }
}
