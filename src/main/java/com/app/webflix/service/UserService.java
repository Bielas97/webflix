package com.app.webflix.service;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.PaymentDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.entity.User;
import com.app.webflix.repository.UserRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private MultimediaService multimediaService;
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private EmailServiceImpl emailService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, MultimediaService multimediaService, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.multimediaService = multimediaService;
        this.emailService = emailService;
    }

    public void addOrUpdateUser(UserDto userDto) {
        LOGGER.info("Adding/updating user");
        LOGGER.debug("Setting user's "+userDto.getUsername()+" date of account creation");
        userDto.setDateTime(LocalDateTime.now());
        LOGGER.debug("Saving user in the database");
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    public void addToWatchList(UserDto userDto, MultimediaDto multimediaDto) {
        LOGGER.info("Adding "+multimediaDto.getName()+" to "+userDto.getUsername()+"'s watchlist");
        if (userDto.getWatchList() == null) {
            LOGGER.debug("Empty watchlist, creating arraylist");
            List<MultimediaDto> multimedias = new ArrayList<>();
            multimedias.add(multimediaDto);
            userDto.setWatchList(multimedias);
        }
        if (!userDto.getWatchList().contains(multimediaDto)) {
            userDto.getWatchList().add(multimediaDto);
            addOrUpdateUser(userDto);
        }else{
            LOGGER.error("Error adding movie to watchlist, probably exists there");
        }
    }

    public UserDto getByUsername(String username) {
        LOGGER.info("Getting "+username+" info");
        return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
    }

    public void deleteUserbyUsername(String username) {
        LOGGER.info("Deleting "+username);
        userRepository.deleteByUsername(username);
    }

    public List<UserDto> getAll() {
        LOGGER.info("Getting all users");
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        LOGGER.info("Deleting user by id:"+id);
        userRepository.deleteById(id);
    }

    public void remove(UserDto userDto) {
        LOGGER.info("Removing user "+userDto.getUsername());
        userRepository.delete(modelMapper.map(userDto, User.class));
    }

    public void addSuggestedContent(MultimediaDto multimediaDto, UserDto userDto, Map<UserDto, Set<MultimediaDto>> favouriteGenres) {
        if (favouriteGenres == null) {
            favouriteGenres = new HashMap<>();
        }
        if (favouriteGenres.get(userDto) == null || favouriteGenres.get(userDto).isEmpty()) {
            Set<MultimediaDto> multimediaGenre = new HashSet<>();
            multimediaGenre.add(multimediaDto);
            favouriteGenres.put(userDto, multimediaGenre);
        } else {
            Set<MultimediaDto> multimediaGenre = favouriteGenres.get(userDto);
            multimediaGenre.add(multimediaDto);
            favouriteGenres.put(userDto, multimediaGenre);
        }
    }

    public void deleteFromWatchList(UserDto userDto, Long id){
        LOGGER.info("Deleting from "+userDto.getUsername()+"'s watchlist:"+id);
        Optional<MultimediaDto> multimediaDto = multimediaService.getOneMultimedia(id);
        multimediaDto.ifPresent(multimediaDto1 -> userDto.getWatchList().remove(multimediaDto1));
        addOrUpdateUser(userDto);
    }

    public void setPayment(UserDto user) {
        PaymentDto paymentDto = PaymentDto.builder()
                .value(99.99)
                .dueDate(LocalDateTime.now().plusDays(1))
                .isPaid(false)
                .user(user)
                .build();
        user.setPayment(paymentDto);
    }

    public void sendMessageAboutPayment(UserDto userDto){
        if( userDto.getPayment() != null && userDto.getPayment().getDueDate().isAfter(LocalDateTime.now()) && !userDto.getPayment().getIsPaid()){
            emailService.sendSimpleMessage(userDto.getUsername(), "WEBFLIX", "Your account is expired, please pay 99.99 US $");
        }
    }

    public boolean isUsernameInDb(String username) {
        return getAll()
                .stream()
                .anyMatch(userDto -> userDto.getUsername().equals(username));
    }

}
