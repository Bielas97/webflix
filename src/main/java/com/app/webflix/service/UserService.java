package com.app.webflix.service;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.entity.User;
import com.app.webflix.repository.UserRepository;
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

    public UserService(UserRepository userRepository, ModelMapper modelMapper, MultimediaService multimediaService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.multimediaService = multimediaService;
    }

    public void addOrUpdateUser(UserDto userDto) {
        userDto.setDateTime(LocalDateTime.now());
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    public void addToWatchList(UserDto userDto, MultimediaDto multimediaDto) {
        if (userDto.getWatchList() == null) {
            List<MultimediaDto> multimedias = new ArrayList<>();
            multimedias.add(multimediaDto);
            userDto.setWatchList(multimedias);
        }
        if (!userDto.getWatchList().contains(multimediaDto)) {
            userDto.getWatchList().add(multimediaDto);
            addOrUpdateUser(userDto);
        }
    }

    public UserDto getByUsername(String username) {
        return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
    }

    public void deleteUserbyUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void remove(UserDto userDto) {
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
        Optional<MultimediaDto> multimediaDto = multimediaService.getOneMultimedia(id);
        multimediaDto.ifPresent(multimediaDto1 -> userDto.getWatchList().remove(multimediaDto1));
        addOrUpdateUser(userDto);
    }

}
