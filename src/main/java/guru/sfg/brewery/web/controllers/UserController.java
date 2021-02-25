package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.seurity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user/")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("register2fa")
    public String register2fa(Model model) {

        model.addAttribute("googleurl", "todo");

        return "user/register2fa";
    }

    @PostMapping("register2fa")
    public String confirm2fa(@RequestParam Integer verifyCode) {

        return "index";
    }
}
