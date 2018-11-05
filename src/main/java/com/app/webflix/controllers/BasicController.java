package com.app.webflix.controllers;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.enums.Role;
import com.app.webflix.service.MultimediaService;
import com.app.webflix.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class BasicController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private MultimediaService multimediaService;

    public BasicController(UserService userService, PasswordEncoder passwordEncoder, MultimediaService multimediaService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.multimediaService = multimediaService;
    }


    @GetMapping("/")
    public String index(Model model, Principal principal){
        model.addAttribute("user", principal.getName());
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addOrUpdateUser(user);
        return "redirect:/";
    }

    @GetMapping("/showMovies")
    public String getAllContent(Model model){
        model.addAttribute("movies", multimediaService.getAll());
        return "getAllMovies";
    }

    @GetMapping("/movie/show/{id}")
    public String showOneMovie(@PathVariable Long id, Model model){
        model.addAttribute("movie", multimediaService.getOneMultimedia(id).get());
        return "showOneMovie";
    }

    @GetMapping("/addMultimedia")
    public String addMultimedia(Model model){
        model.addAttribute("movie", new MultimediaDto());
        return "addMultimedia";
    }

    @PostMapping("/addMultimedia")
    public String addMultimediaPost(@ModelAttribute MultimediaDto multimediaDto){
        System.out.println("=++++++++++++++++++++++++++");
        multimediaDto.setDateTime(LocalDateTime.now());
        multimediaService.addOrUpdateMultimedia(multimediaDto);
        return "redirect:/";
    }

    @PostMapping("/movie/show/{id}")
    public String toWatchList(@PathVariable Long id, Principal principal){
        System.out.println("++++++++++++++++++++++");
        multimediaService.getOneMultimedia(id).ifPresent(multimediaDto -> {
            UserDto userDto = userService.getByUsername(principal.getName());
            userService.addToWatchList(userDto, multimediaDto);
            System.out.println(userService.getByUsername(principal.getName()));
        });
        return "redirect:/";
    }
}
