package guru.sfg.brewery.security.google;

import guru.sfg.brewery.domain.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && trustResolver.isAnonymous(auth)) {
            log.debug("Processing 2fa filter");
            User user = (User) auth.getPrincipal();
            if(user.getUseGoogle2FA() && user.getGoogle2FARequired()) {
                log.debug("2FA Required");

                google2FAFailureHandler.onAuthenticationFailure(request, response, null);
            }
        }


        filterChain.doFilter(request, response);
    }
}
