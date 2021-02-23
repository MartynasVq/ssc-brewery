package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class BeerOrderAuthenticationManager {


    public boolean customerIdMatches(Authentication auth, UUID uuid) {
        User authenticatedUser = (User) auth.getPrincipal();

        return authenticatedUser.getCustomer().getId().equals(uuid);
    }
}
