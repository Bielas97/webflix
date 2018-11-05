package com.app.webflix.controllers;

import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.enums.Role;
import com.app.webflix.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BasicController {
    private UserService userService;

    public BasicController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "loginForm";
    }


    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @PostMapping
    public String registerUserPost(@ModelAttribute UserDto user, Model model){
        userService.addOrUpdateUser(user);
        return "redirect:/";
    }
}
