package guru.sfg.brewery.config;

import guru.sfg.brewery.security.MyPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvc implements WebMvcConfigurer {

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return MyPasswordEncoder.createDelegatingPasswordEncoder();
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT").allowedOrigins("" +//www.
                "*");
    }
}
