package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @Test
    void findSecuredListUnauthorised() throws Exception {
        mockMvc.perform(get("/customers/find")).andExpect(status().isUnauthorized());
    }

    @Test
    void findSecuredListForbidden() throws Exception {
        mockMvc.perform(get("/customers/find")
        .with(httpBasic("user", "user"))).andExpect(status().isForbidden());
    }

    @Test
    void findSecuredListOk() throws Exception {
        mockMvc.perform(get("/customers/find")
                .with(httpBasic("admin2", "admin2"))).andExpect(status().isOk());
    }

    @Nested
    class createTest {


        @Test
        void createCustomerIsOk() throws Exception {
            mockMvc.perform(post("/customers/new").param("customerName", "alex")
                    .with(httpBasic("admin2","admin2"))).andExpect(status().is3xxRedirection());
        }

        @Test
        void createCustomerUnauthorised() throws Exception {
            mockMvc.perform(post("/customers/new").param("customerName", "alex")
                    ).andExpect(status().isUnauthorized());
        }

        @Test
        void createCustomerForbidden() throws Exception {
            mockMvc.perform(post("/customers/new").param("customerName", "alex")
            .with(httpBasic("user","user"))).andExpect(status().isForbidden());
        }


    }
}
