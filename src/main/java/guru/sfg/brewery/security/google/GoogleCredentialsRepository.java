package guru.sfg.brewery.security.google;

import com.warrenstrange.googleauth.ICredentialRepository;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.seurity.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleCredentialsRepository implements ICredentialRepository {

    private final UserRepository userRepository;

    @Override
    public String getSecretKey(String s) {
        User user = userRepository.findByUsername(s).orElseThrow();

        return user.getGoogle2FASecret();
    }

    @Override
    public void saveUserCredentials(String s, String s1, int i, List<Integer> list) {
        User user = userRepository.findByUsername(s).orElseThrow();

        user.setGoogle2FASecret(s1);
        user.setUseGoogle2FA(true);
        userRepository.save(user);
    }
}
