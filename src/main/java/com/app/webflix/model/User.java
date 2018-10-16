package com.app.webflix.model;

import com.app.webflix.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String mail;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}
