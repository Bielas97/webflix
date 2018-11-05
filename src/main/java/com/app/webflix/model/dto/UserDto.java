package com.app.webflix.model.dto;

import com.app.webflix.model.entity.Multimedia;
import com.app.webflix.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String creditCard;
    private Role role;
    private LocalDateTime dateTime;
    private List<Multimedia> watchList;
}
