package guru.sfg.brewery.security.google;

import guru.sfg.brewery.domain.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class Google2FAFilter extends GenericFilterBean {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private final Google2FAFailureHandler google2FAFailureHandler = new Google2FAFailureHandler();
    private final RequestMatcher urlIs2fa = new AntPathRequestMatcher("/user/verify2fa");
    private final RequestMatcher urlResource = new AntPathRequestMatcher("/resources/**");



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        StaticResourceRequest.StaticResourceRequestMatcher staticResourceRequestMatcher = PathRequest.toStaticResources().atCommonLocations();

        if(urlIs2fa.matches(request) || urlResource.matches(request) || staticResourceRequestMatcher.matcher(request).isMatch()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && !trustResolver.isAnonymous(auth)) {
            log.debug("Processing 2fa filter");
            User user;
            if(auth.getPrincipal() instanceof User) {
                user = (User) auth.getPrincipal();
                log.debug("Object is user");
                log.debug("Use 2 FA " + user.getUseGoogle2FA());
                log.debug("Use Google FA required " + user.getGoogle2FARequired());
                if (user.getUseGoogle2FA() && user.getGoogle2FARequired()) {
                    log.debug("2FA Required");

                    google2FAFailureHandler.onAuthenticationFailure(request, response, null);
                    return;
                }
            }
        }


        filterChain.doFilter(request, response);
    }
}
