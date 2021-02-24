package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.MyPasswordEncoder;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestParamAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.http.HttpRequest;
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    //for spring data
    @Bean
    SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter restHeaderAuthFilter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        restHeaderAuthFilter.setAuthenticationManager(authenticationManager);
        return restHeaderAuthFilter;
    }

    public RestParamAuthFilter restParamAuthFilter(AuthenticationManager authenticationManager) {
        RestParamAuthFilter restParamAuthFilter = new RestParamAuthFilter(new AntPathRequestMatcher("/api/**"));
        restParamAuthFilter.setAuthenticationManager(authenticationManager);
        return restParamAuthFilter;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return MyPasswordEncoder.createDelegatingPasswordEncoder();
    }
    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).addFilterBefore(
                        restParamAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().ignoringAntMatchers("/h2-console/**", "/api/**");

        http.authorizeRequests(auth -> {
            auth.antMatchers("/", "/webjars/**", "/resources/**").permitAll();
            auth.antMatchers("/h2-console/**").permitAll();
        })
                .authorizeRequests().anyRequest().authenticated().and().formLogin(loginConfigurer -> {
                    loginConfigurer.loginProcessingUrl("/login")
                            .loginPage("/").permitAll()
                            .failureForwardUrl("/")
                            .defaultSuccessUrl("/")
                            .failureUrl("/?error");
        })
                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/?logout") //param can be used in th forms
                            .permitAll();
                }).httpBasic()
        .and().rememberMe().tokenRepository(persistentTokenRepository).userDetailsService(userDetailsService);

        //in memory config for tokens
        //.and().rememberMe().tokenValiditySeconds(100000000).key("L2BZ").userDetailsService(userDetailsService);

        //h2 console config
        http.headers().frameOptions().sameOrigin();
    }



//    //Overriding default spring config for user
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin1").password("admin1")
//                .roles("ADMIN").build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user1").password("user2")
//                .roles("USER").build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
//
//    //alternative way of initializing the user


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* Encoder option 1
        PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication().withUser("admin2")
                .password(pe.encode("admin2")).roles("ADMIN");
        */

//        auth.inMemoryAuthentication().withUser("admin2")
//                .password("{bcrypt11}$2a$11$BxZmrxKEGLZL1jY9PS0nXOdjtfFaTvE1RWtHvS6r/d/jqG0YA1X9S").roles("ADMIN").and().withUser("scott")
//        .password("{bcrypt11}$2a$11$v1mnKLiBfVEsqIMRIRRkoeBNjnUZLEnRkQBe1YAkdEIHdFYMzTyq6").roles("CUSTOMER");

        //jpa validation - also not needed , spring will automatically load in our custom user service and password encoder
        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
    }

}
