package ru.ripgor.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ripgor.security.model.User;
import ru.ripgor.security.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUserInfo(Principal principal, Model model) {
        User user = userService.getUser(principal.getName());

        model.addAttribute("user", user);

        return "user_page";
    }
}
