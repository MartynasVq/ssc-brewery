package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class AuthSuccessListener {

    @EventListener
    public void listen(AuthenticationSuccessEvent authenticationSuccessEvent) {
        log.debug("User authenticated.");

        if(authenticationSuccessEvent.getSource() instanceof UsernamePasswordAuthenticationToken)
        {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authenticationSuccessEvent.getSource();
                if(token.getPrincipal() instanceof User)
                {
                    User user = (User) token.getPrincipal();
                    log.debug("User " + user.getUsername() + " logged in.");
                }
                if(token.getDetails() instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails wad = (WebAuthenticationDetails) token.getDetails();
                    log.debug("IP " + wad.getRemoteAddress());
                }

        }
    }
}
