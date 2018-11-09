package com.app.webflix.model.dto;

import com.app.webflix.model.entity.Multimedia;
import com.app.webflix.model.entity.Payment;
import com.app.webflix.model.enums.Role;
import lombok.*;

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
    @EqualsAndHashCode.Exclude
    private PaymentDto payment;

    private List<MultimediaDto> watchList;

    private List<String> favouriteGenre;
}
