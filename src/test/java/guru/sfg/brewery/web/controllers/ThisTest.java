package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ThisTest {

    @Test
    void test() {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder(11);
        System.out.println(b.encode("admin2"));
    }

}
