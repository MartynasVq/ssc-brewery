package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.seurity.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUnlockService {

    private final UserRepository userRepository;
    @Scheduled(fixedRate = 5000L)
    public void unlockAccounts() {
        log.debug("Unlocker running");

        List<User> users = userRepository.findAllAccountNonLockedAndLastModifiedDateIsBefore(false, Timestamp.valueOf(
                LocalDateTime.now().minusMinutes(1)));

        if(users.size() > 0) {
            log.debug("Locked Accounts Found.");
            users.forEach(e-> e.setAccountNonLocked(true));
        }

        userRepository.saveAll(users);

    }
}
