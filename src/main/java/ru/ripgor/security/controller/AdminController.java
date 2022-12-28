package ru.ripgor.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ripgor.security.model.User;
import ru.ripgor.security.repository.RoleRepository;
import ru.ripgor.security.service.UserService;

import java.security.Principal;

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
    public String getAdminPage(Principal principal, Model model) {
        model.addAttribute("user", userService.getUser(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/admin_page";
    }

    @GetMapping("/create")
    public String getCreatePage(Principal principal, Model model) {
        model.addAttribute("user", userService.getUser(principal.getName()));
        model.addAttribute("roles", roleRepository.findAll());

        return "admin/create_page";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);

        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PutMapping("/update/{id}")
    public String updateUser(User user) {
        userService.updateUser(user);

        return "redirect:/admin";
    }

}
