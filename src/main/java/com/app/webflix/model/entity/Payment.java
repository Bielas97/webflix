package com.app.webflix.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    private Double value;
    private Boolean isPaid;
    private LocalDateTime dueDate;
    @OneToOne(mappedBy = "payment")
    @EqualsAndHashCode.Exclude
    private User user;
}
