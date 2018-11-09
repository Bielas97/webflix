package com.app.webflix.security;


import com.app.webflix.model.entity.User;
import com.app.webflix.model.enums.Role;
import com.app.webflix.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class);
    public UserDetailsServiceImpl(UserRepository userRepository) {
        LOGGER.debug("Creating UserDetailsServiceImpl object...");
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOGGER.debug("User was not found...");
            throw new UsernameNotFoundException("USERNAME NOT FOUND");
        }

        return new org.springframework.security.core.userdetails.User(

                user.getUsername(),
                user.getPassword(),
                true, true, true, true,
                getAuthorities(user.getRole())
        );
    }

    private Collection<GrantedAuthority> getAuthorities(Role role) {
        LOGGER.debug("Getting authority");
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
