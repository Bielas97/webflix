package com.app.webflix.model.entity;

import com.app.webflix.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String creditCard;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime dateTime;
}
