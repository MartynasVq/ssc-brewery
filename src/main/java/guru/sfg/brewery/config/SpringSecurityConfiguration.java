package guru.sfg.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> {
            auth.antMatchers("/", "/webjars/**", "/resources/**").permitAll();
            auth.antMatchers("/beers/find").permitAll();
            auth.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
        })
                .authorizeRequests().anyRequest().authenticated().and().formLogin()
                .and().httpBasic();
    }
}
