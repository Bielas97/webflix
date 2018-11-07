package com.app.webflix.controllers;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.enums.Role;
import com.app.webflix.service.MultimediaService;
import com.app.webflix.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BasicController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private MultimediaService multimediaService;

    Map<UserDto, Set<MultimediaDto>> favouriteGenres = new HashMap<>();

    public BasicController(UserService userService, PasswordEncoder passwordEncoder, MultimediaService multimediaService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.multimediaService = multimediaService;
    }


    @GetMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("user", principal.getName());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "loginForm";
    }


    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserPost(@ModelAttribute UserDto user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userService.addOrUpdateUser(user);
        return "redirect:/";
    }

    @GetMapping("/admin/registration")
    public String registerAdmin(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/admin/registration")
    public String registerAdminPost(@ModelAttribute UserDto user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.MANAGER);
        userService.addOrUpdateUser(user);
        return "redirect:/";
    }

    @GetMapping("/showMovies")
    public String getAllContent(Model model) {
        model.addAttribute("movies", multimediaService.getAll());
        return "getAllMovies";
    }

    @GetMapping("/addMultimedia")
    public String addMultimedia(Model model) {
        model.addAttribute("movie", new MultimediaDto());
        return "addMultimedia";
    }

    @PostMapping("/addMultimedia")
    public String addMultimediaPost(@ModelAttribute MultimediaDto multimediaDto) {
        System.out.println("=++++++++++++++++++++++++++");
        multimediaDto.setDateTime(LocalDateTime.now());
        multimediaService.addOrUpdateMultimedia(multimediaDto);
        return "redirect:/";
    }

    @GetMapping("/showMovie/{id}")
    public String showOneMovie(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("movie", multimediaService.getOneMultimedia(id).get());
        multimediaService.getOneMultimedia(id).ifPresent(multimediaDto -> {
            UserDto userDto = userService.getByUsername(principal.getName());
            userService.addSuggestedContent(multimediaDto, userDto, favouriteGenres);
        });
        return "showOneMovie";
    }

    @GetMapping("/movie/toWatchlist/{id}")
    public String addToWatchList(@PathVariable Long id, Principal principal){
        System.out.println("+++++++++++");
        UserDto userDto = userService.getByUsername(principal.getName());
        multimediaService.getOneMultimedia(id).ifPresent(multimediaDto1 -> userService.addToWatchList(userDto, multimediaDto1));
        System.out.println(userDto);
        return "redirect:/showMovies";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "deleteUser";
    }

    @PostMapping("/deleteUser")
    public String deleteUserPost(@ModelAttribute UserDto userDto) {

        Optional<UserDto> user = userService.getAll()
                .stream()
                .filter(us -> us.getUsername().equals(userDto.getUsername()) && us.getPassword().equals(userDto.getPassword()))
                .findFirst();
        System.out.println("tu powinno");
        user.ifPresent(System.out::println);
        return "redirect:/";
    }

    @GetMapping("/suggestedContent")
    public String showSuggestedContent(Model model, Principal principal) {
        if (favouriteGenres == null || favouriteGenres.get(userService.getByUsername(principal.getName())) == null) {
            model.addAttribute("isnull", true);
        } else {
            model.addAttribute("isnull", false);
            model.addAttribute("suggestedMovies", favouriteGenres.get(userService.getByUsername(principal.getName())));
        }
        return "suggestedContent";
    }

    @GetMapping("/watchList")
    public String showWatchList(Model model, Principal principal){
        UserDto userDto = userService.getByUsername(principal.getName());
        if(userDto.getWatchList() == null || userDto.getWatchList().isEmpty()){
            model.addAttribute("isnull", true);
        }
        else {
            model.addAttribute("movies", userDto.getWatchList());
            model.addAttribute("isnull", false);
        }
        return "watchList";
    }

    @GetMapping("/remove/from/watchlist/{id}")
    public String removeWatchList(@PathVariable Long id, Principal principal){
        userService.deleteFromWatchList(userService.getByUsername(principal.getName()), id);
        return "redirect:/";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
