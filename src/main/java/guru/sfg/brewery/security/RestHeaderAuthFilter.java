package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestHeaderAuthFilter extends AbstractAuthenticationProcessingFilter {


    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String username = getUsername(httpServletRequest);
        String password = getPassword(httpServletRequest);

        if(username == null) {
            username = "";
        }
        if(password == null) {
            password = "";
        }

        log.debug(username);
        log.debug(password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(token);
    }

    private String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("api-secret");
    }

    private String getUsername(HttpServletRequest http) {
        return http.getHeader("api-key");
    }
}
