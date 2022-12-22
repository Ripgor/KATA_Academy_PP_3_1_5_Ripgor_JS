package ru.ripgor.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ripgor.security.model.Role;
import ru.ripgor.security.model.User;
import ru.ripgor.security.repository.RoleRepository;
import ru.ripgor.security.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "index";
    }

    @GetMapping("/create")
    public String getFormForCreateUser(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        return "create";
    }

    @PostMapping("/create")
    public String createUser(@Valid User user, BindingResult result, Model model) {
        User userFromDB = userService.getUser(user.getId());

        if (result.hasErrors()) {
            List<Role> roles = roleRepository.findAll();

            model.addAttribute("roles", roles);
            model.addAttribute("user", user);
            return "create";
        }

        if (userFromDB != null) {
            model.addAttribute("exists", "User Exists");
            return "create";
        }

        userService.saveUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);

        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String getFormForUpdateUser(@PathVariable("id") int id, Model model) {
        User user = userService.getUser(id);
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("roles", roles);
        model.addAttribute("user", user);

        return "update";
    }

    @PatchMapping("/update")
    public String updateUser(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            List<Role> roles = roleRepository.findAll();

            model.addAttribute("roles", roles);
            model.addAttribute("user", user);
            return "update";
        }

        userService.updateUser(user);

        return "redirect:/admin";
    }

}
