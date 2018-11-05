package com.app.webflix.model.dto;

import com.app.webflix.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String creditCard;
    private Role role;
    private LocalDateTime dateTime;
}
