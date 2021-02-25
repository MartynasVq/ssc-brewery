package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.seurity.LoginFailureRepository;
import guru.sfg.brewery.repositories.seurity.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.sql.Timestamp.valueOf;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("Login failure");

        if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if(token.getPrincipal() instanceof String) {
                userRepository.findByUsername((String) token.getPrincipal()).ifPresent(builder::user);
                log.debug("Failed authentication for user " + token.getPrincipal());
            }
            if(token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails wad = (WebAuthenticationDetails) token.getDetails();
                builder.sourceIp(wad.getRemoteAddress());
                log.debug("IP: " + wad.getRemoteAddress());
            }

            LoginFailure loginFailure = loginFailureRepository.save(builder.build());

            if(loginFailure.getUser() != null)
                lockUserAccount(loginFailure.getUser());
        }
    }

    private void lockUserAccount(User user) {
        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user,
                valueOf(LocalDateTime.now().minusDays(1)));
        if(failures.size() > 3) {
            log.debug("Locking user account.");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
