package com.app.webflix.validators;

import com.app.webflix.model.dto.UserDto;
import com.app.webflix.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class UserValidator implements Validator {
    UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UserDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDto userDto = (UserDto) o;

        List<UserDto> userDtoList = userService.getAll()
                .stream()
                .filter(us -> us.getUsername().equals(userDto.getUsername()))
                .collect(Collectors.toList());

        if(!userDtoList.isEmpty()){
            errors.rejectValue("mail", "mail already in use");
        }

        //Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
        Matcher matcher = pattern.matcher(userDto.getUsername());
        if(matcher.find()){
            errors.rejectValue("username", "incorrect mail");
        }

    }
}
