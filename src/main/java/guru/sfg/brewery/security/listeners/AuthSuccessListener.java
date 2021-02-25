package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginSuccess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.seurity.LoginSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthSuccessListener {

    private final LoginSuccessRepository loginSuccessRepository;

    @EventListener
    public void listen(AuthenticationSuccessEvent authenticationSuccessEvent) {
        log.debug("User authenticated.");

        if(authenticationSuccessEvent.getSource() instanceof UsernamePasswordAuthenticationToken)
        {
            LoginSuccess.LoginSuccessBuilder builder = LoginSuccess.builder();
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authenticationSuccessEvent.getSource();
                if(token.getPrincipal() instanceof User)
                {
                    User user = (User) token.getPrincipal();
                    builder.user(user);
                    log.debug("User " + user.getUsername() + " logged in.");
                }
                if(token.getDetails() instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails wad = (WebAuthenticationDetails) token.getDetails();
                    log.debug("IP " + wad.getRemoteAddress());
                    builder.sourceIp(wad.getRemoteAddress());
                }

                LoginSuccess loginSuccess = loginSuccessRepository.save(builder.build());
        }
    }
}
