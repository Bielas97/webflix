package com.app.webflix.service;

import com.app.webflix.model.User;
import com.app.webflix.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addOrUpdateUser(User user) {
        userRepository.save(user);
    }
}
