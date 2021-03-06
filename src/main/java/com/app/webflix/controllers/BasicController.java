package com.app.webflix.controllers;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.enums.Role;
import com.app.webflix.service.MultimediaService;
import com.app.webflix.service.UserService;
import com.app.webflix.validators.UserValidator;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BasicController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private MultimediaService multimediaService;

    private static final Logger LOGGER = Logger.getLogger(BasicController.class);

    Map<UserDto, Set<MultimediaDto>> favouriteGenres = new HashMap<>();


    public BasicController(UserService userService, PasswordEncoder passwordEncoder, MultimediaService multimediaService) {
        LOGGER.debug("Creating BasicController object");
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.multimediaService = multimediaService;
    }


    @GetMapping("/")
    public String index(Model model, Principal principal) {
        LOGGER.debug("Index site returned");
        UserDto userDto = userService.getByUsername(principal.getName());
        userService.sendMessageAboutPayment(userDto);
        model.addAttribute("user", principal.getName());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LOGGER.debug("Returned to login page");
        model.addAttribute("error", "");
        return "loginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        LOGGER.info("User inputted wrong credentials");
        final String loginErrorMessage = "Nieprawidłowe dane logowania";
        model.addAttribute("error", loginErrorMessage);
        return "loginForm";
    }


    @GetMapping("/register")
    public String registerUser(Model model) {
        LOGGER.info("Redirecting to register page");
        model.addAttribute("user", new UserDto());
        model.addAttribute("error", false);
        return "register";
    }

    @PostMapping("/register")
    public String registerUserPost(@ModelAttribute UserDto user, Model model) {
        System.out.println(userService.isUsernameInDb(user.getUsername()));
        if(userService.isUsernameInDb(user.getUsername())){
            LOGGER.info("Redirecting to register page");
            model.addAttribute("user", new UserDto());
            model.addAttribute("error", true);
            return "register";
        }
        else {

            LOGGER.info("Registering user");
            LOGGER.debug("Setting password");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LOGGER.debug("Setting role");
            user.setRole(Role.USER);
            userService.setPayment(user);
            userService.addOrUpdateUser(user);
            LOGGER.debug("Redirecting to /");
            return "redirect:/";
        }

    }

    @GetMapping("/admin/registration")
    public String registerAdmin(Model model) {
        LOGGER.info("Redirecting to admin register page");
        model.addAttribute("user", new UserDto());
        model.addAttribute("error", false);
        return "registerAdmin";
    }

    @PostMapping("/admin/registration")
    public String registerAdminPost(@ModelAttribute UserDto user, Model model) {
        if(userService.isUsernameInDb(user.getUsername())){
            LOGGER.info("Redirecting to admin register page");
            model.addAttribute("user", new UserDto());
            model.addAttribute("error", false);
            model.addAttribute("error", true);
            return "registerAdmin";
        }
        else {
            LOGGER.info("Registering admin");
            LOGGER.debug("Setting admin's password");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LOGGER.debug("Setting admin's role");
            user.setRole(Role.ADMIN);
            System.out.println(user);
            userService.addOrUpdateUser(user);
            return "redirect:/";
        }
    }

    @GetMapping("/showMovies")
    public String getAllContent(Model model) {
        LOGGER.debug("Showing page with all movies");
        model.addAttribute("movies", multimediaService.getAll());
        return "getAllMovies";
    }

    @GetMapping("/sortByNames")
    public String getAllContentSortedByName(Model model) {
        LOGGER.debug("getting all content sorted by name");
        List<MultimediaDto> multimediaDtos = multimediaService.getAll()
                .stream()
                .sorted(Comparator.comparing(MultimediaDto::getName))
                .collect(Collectors.toList());
        model.addAttribute("movies", multimediaDtos);
        return "getAllMovies";
    }

    @GetMapping("/sortByNamesDescending")
    public String getAllContentSortedByNameDescending(Model model) {
        LOGGER.debug("getting all content sorted by name");
        List<MultimediaDto> multimediaDtos = multimediaService.getAll()
                .stream()
                .sorted((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .collect(Collectors.toList());
        model.addAttribute("movies", multimediaDtos);
        return "getAllMovies";
    }

    @GetMapping("/addMultimedia")
    public String addMultimedia(Model model) {
        LOGGER.debug("Showing page with multimedia adding");
        model.addAttribute("movie", new MultimediaDto());
        return "addMultimedia";
    }

    @PostMapping("/addMultimedia")
    public String addMultimediaPost(@ModelAttribute MultimediaDto multimediaDto) {
        LOGGER.debug("Setting date of creation of multimedia");
        multimediaDto.setDateTime(LocalDateTime.now());
        multimediaService.addOrUpdateMultimedia(multimediaDto);
        return "redirect:/";
    }

    @GetMapping("/showMovie/{id}")
    public String showOneMovie(@PathVariable Long id, Model model, Principal principal) {
        LOGGER.info("Showing one movie: "+ multimediaService.getOneMultimedia(id).get());
        model.addAttribute("movie", multimediaService.getOneMultimedia(id).get());multimediaService.getOneMultimedia(id).ifPresent(multimediaDto -> {
            UserDto userDto = userService.getByUsername(principal.getName());
            userService.addSuggestedContent(multimediaDto, userDto, favouriteGenres);
        });
        return "showOneMovie";
    }

    @GetMapping("/user/update")
    public String updateUser(Model model, Principal principal){
        LOGGER.debug("updating user data...");
        model.addAttribute("user", userService.getByUsername(principal.getName()));
        model.addAttribute("error", false);
        return "updateUser";
    }

    @PostMapping("/user/update")
    public String updateUserPost(@ModelAttribute UserDto userDto){
        LOGGER.debug("user update request");
        userService.addOrUpdateUser(userDto);
        return "redirect:/";
    }

    @GetMapping("/updateMovie/{id}")
    public String updateMovie(@PathVariable Long id, Model model) {
        LOGGER.info("Redirecting to movie update page");
        model.addAttribute("movie", multimediaService.getOneMultimedia(id).get());
        return "updateMovie";
    }

    @PostMapping("/updateMovie")
    public String updateMoviePost(MultimediaDto multimediaDto) {
        LOGGER.debug("Movie update request");
        multimediaDto.setDateTime(LocalDateTime.now());
        multimediaService.addOrUpdateMultimedia(multimediaDto);
        return "redirect:/showMovies";
    }

    @GetMapping("/deleteMovie/{id")
    public String deleteMovie(@PathVariable Long id) {
        LOGGER.info("Redirecting to movie deletion page");
        multimediaService.deleteMultimedia(id);
        return "redirect:/showMovies";
    }

    @GetMapping("/movie/toWatchlist/{id}")
    public String addToWatchList(@PathVariable Long id, Principal principal) {
        LOGGER.info("Redirecting to movie watchlist details page");
        UserDto userDto = userService.getByUsername(principal.getName());
        multimediaService.getOneMultimedia(id).ifPresent(multimediaDto1 -> userService.addToWatchList(userDto, multimediaDto1));
        LOGGER.debug(userDto);
        return "redirect:/showMovies";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Model model) {
        LOGGER.info("Redirecting to user deletion page");
        model.addAttribute("user", new UserDto());
        return "deleteUser";
    }

    @PostMapping("/deleteUser")
    public String deleteUserPost(@ModelAttribute UserDto userDto) {
        LOGGER.debug("Request to delete user");
        Optional<UserDto> user = userService.getAll()
                .stream()
                .filter(us -> us.getUsername().equals(userDto.getUsername()) && us.getPassword().equals(userDto.getPassword()))
                .findFirst();
        System.out.println(userDto);
        List<UserDto> usersWithUsername = userService.getAll().stream()
                .filter(userDto1 -> userDto1.getUsername().equals(userDto.getUsername()) /*&& userDto1.getPassword().equals(userDto.getPassword())*/)
                .collect(Collectors.toList());
        System.out.println(usersWithUsername.get(0).getId());
        System.out.println("----------------------");
        user.ifPresent(System.out::println);
        return "redirect:/";
    }

    @GetMapping("/suggestedContent")
    public String showSuggestedContent(Model model, Principal principal) {
        LOGGER.info("Redirecting to suggested content page");
        if (favouriteGenres == null || favouriteGenres.get(userService.getByUsername(principal.getName())) == null) {
            LOGGER.debug("User has not browsed movies");
            model.addAttribute("isnull", true);
        } else {
            LOGGER.debug("Getting suggested content, it exists");
            model.addAttribute("isnull", false);
            model.addAttribute("suggestedMovies", favouriteGenres.get(userService.getByUsername(principal.getName())));
        }
        return "suggestedContent";
    }

    @GetMapping("/watchList")
    public String showWatchList(Model model, Principal principal) {
        LOGGER.info("Showing watchlist page");
        UserDto userDto = userService.getByUsername(principal.getName());
        if (userDto.getWatchList() == null || userDto.getWatchList().isEmpty()) {
            LOGGER.debug("watchlist is null or empty");
            model.addAttribute("isnull", true);
        } else {
            LOGGER.debug("There is something in the watchlist");
            model.addAttribute("movies", userDto.getWatchList());
            model.addAttribute("isnull", false);
        }
        return "watchList";
    }

    @GetMapping("/remove/from/watchlist/{id}")
    public String removeWatchList(@PathVariable Long id, Principal principal) {
        LOGGER.info("Deleting movie with id:"+ id +" from the watchlist.");
        userService.deleteFromWatchList(userService.getByUsername(principal.getName()), id);
        return "redirect:/";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        LOGGER.info("Access was denied");
        return "accessDenied";
    }

}
