package com.app.webflix;

import com.app.webflix.model.entity.User;
import com.app.webflix.model.enums.Role;
import com.app.webflix.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@SpringBootApplication
public class WebflixApplication implements CommandLineRunner {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public WebflixApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebflixApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.count() == 0) {
			userRepository.save(User.builder().username("mail").password(passwordEncoder.encode("1234")).creditCard("312132").dateTime(LocalDateTime.now()).role(Role.MANAGER).build());
		}
	}
}
