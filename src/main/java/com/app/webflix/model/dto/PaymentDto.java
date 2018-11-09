package com.app.webflix.model.dto;

import com.app.webflix.model.entity.User;
import lombok.*;

import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Double value;
    private Boolean isPaid;
    private LocalDateTime dueDate;
    @EqualsAndHashCode.Exclude
    private UserDto user;
}
