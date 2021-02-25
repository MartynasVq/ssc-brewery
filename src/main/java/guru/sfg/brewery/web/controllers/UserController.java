package guru.sfg.brewery.web.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.seurity.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@RequestMapping("/user/")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;

    @GetMapping("register2fa")
    public String register2fa(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("L2BZ", user.getUsername(), googleAuthenticator.createCredentials());
        log.debug(url);
        model.addAttribute("googleurl", url);

        return "user/register2fa";
    }

    @PostMapping
    public String confirm2fa(@RequestParam Integer verifyCode) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {
            User savedUser = userRepository.findById(user.getId()).orElseThrow();
            savedUser.setUseGoogle2FA(true);
            userRepository.save(savedUser);
            return "index";
        } else {
            return "user/register2fa";
        }


    }
}
