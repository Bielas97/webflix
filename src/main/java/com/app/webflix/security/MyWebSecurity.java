package com.app.webflix.security;

import com.app.webflix.model.enums.Role;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class MyWebSecurity extends WebSecurityConfigurerAdapter{
    private UserDetailsService userDetailsService;
    private static final Logger LOGGER = Logger.getLogger(MyWebSecurity.class);
    public MyWebSecurity(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("Configuring authentication service");
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("1234")).roles(String.valueOf(Role.MANAGER));
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("1234")).roles(String.valueOf(Role.MANAGER));
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/registration").hasAnyRole(String.valueOf(Role.ADMIN))
                .antMatchers("/updateMovie").hasAnyRole(String.valueOf(Role.ADMIN))
                .antMatchers("/deleteMovie").hasAnyRole(String.valueOf(Role.ADMIN))
                .antMatchers("/register").permitAll()
                .antMatchers("/deleteUser").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/app-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login/error")

                .and()
                .logout().permitAll()
                .logoutUrl("/app-logout")
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .httpBasic();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/accessDenied");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        LOGGER.debug("Creating password encoder...");
        return new BCryptPasswordEncoder();
    }
}
