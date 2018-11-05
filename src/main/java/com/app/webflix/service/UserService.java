package com.app.webflix.service;

import com.app.webflix.model.dto.MultimediaDto;
import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.entity.Multimedia;
import com.app.webflix.model.entity.User;
import com.app.webflix.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void addOrUpdateUser(UserDto userDto) {
        userDto.setDateTime(LocalDateTime.now());
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    public void addToWatchList(UserDto userDto, MultimediaDto multimediaDto) {
        if (userDto.getWatchList() == null){
            List<Multimedia> multimedias = new ArrayList<>();
            multimedias.add(modelMapper.map(multimediaDto, Multimedia.class));
            userDto.setWatchList(multimedias);
        }
        userDto.getWatchList().add(modelMapper.map(multimediaDto, Multimedia.class));
        addOrUpdateUser(userDto);
    }

    public UserDto getByUsername(String username){
        return modelMapper.map(userRepository.findByUsername(username), UserDto.class);
    }

    public void deleteUserbyUsername(String username){
        userRepository.deleteByUsername(username);
    }
}
