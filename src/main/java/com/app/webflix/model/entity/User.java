package com.app.webflix.model.entity;

import com.app.webflix.model.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;
    //username jest mailem
    private String username;
    private String password;
    private String creditCard;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime dateTime;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "watchList",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = { @JoinColumn(name = "multimedia_id") }
    )
    private List<Multimedia> watchList;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id")
    @EqualsAndHashCode.Exclude
    private Payment payment;
}
