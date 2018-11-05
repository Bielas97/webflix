package com.app.webflix.service;

import com.app.webflix.model.dto.UserDto;
import com.app.webflix.model.entity.User;
import com.app.webflix.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
