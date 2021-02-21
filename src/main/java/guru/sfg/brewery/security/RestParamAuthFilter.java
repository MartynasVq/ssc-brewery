package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestParamAuthFilter extends AbstractRestAuthFilter {

    public RestParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("api-secret");
    }

    @Override
    protected String getUsername(HttpServletRequest http) {
        return http.getParameter("api-key");
    }
}
