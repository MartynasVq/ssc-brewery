package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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


    //Overriding default spring config for user
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin1").password("admin1")
                .roles("ADMIN").build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user1").password("user2")
                .roles("USER").build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    //alternative way of initializing the user


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* Encoder option 1
        PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication().withUser("admin2")
                .password(pe.encode("admin2")).roles("ADMIN");
        */

        // Encoder option 2
        // noop = plaintext /
        auth.inMemoryAuthentication().withUser("admin2")
                .password("{noop}admin2").roles("ADMIN").and().withUser("scott")
        .password("{noop}tiger").roles("CUSTOMER");
    }

}
